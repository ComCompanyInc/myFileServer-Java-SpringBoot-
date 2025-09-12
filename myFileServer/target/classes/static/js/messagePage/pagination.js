// script.js
document.addEventListener('DOMContentLoaded', function() {
    const API_BASE = 'http://localhost:8080';
    const PAGE_SIZE = 20;
    
    // Элементы DOM
    const messagesList = document.getElementById('messages-list');
    const paginationContainer = document.getElementById('pagination');
    const messageForm = document.getElementById('message-form');
    const formMessage = document.getElementById('form-message');
    
    let currentPage = 0;
    let currentUser = 'demoUser'; // В реальном приложении получаем из аутентификации
    
    // Инициализация
    function init() {
        loadMessages(currentPage);
        messageForm.addEventListener('submit', handleFormSubmit);
    }
    
    // Загрузка сообщений
    async function loadMessages(page) {
        try {
            const response = await fetch(`${API_BASE}/messages?page=${page}&size=${PAGE_SIZE}`, {
                credentials: 'include'
            });
            
            if (!response.ok) throw new Error(`Ошибка HTTP: ${response.status}`);
            
            const messages = await response.json();
            displayMessages(messages);
            updatePagination(page);
            
        } catch (error) {
            console.error('Ошибка при загрузке сообщений:', error);
            messagesList.innerHTML = '<div class="error">Не удалось загрузить сообщения</div>';
        }
    }
    
    // Отображение сообщений
    function displayMessages(messages) {
        if (!messages || messages.length === 0) {
            messagesList.innerHTML = '<div class="message-item">У вас нет сообщений</div>';
            return;
        }
        
        messagesList.innerHTML = messages.map(message => `
            <div class="message-item">
                <div class="message-header">
                    <span class="message-sender">От: ${message.sender.login || 'Неизвестно'}</span>
                    <span class="message-date">${message.sendDate}</span>
                </div>
                <div class="message-content">${message.description || ''}</div>
                ${message.fileUrl ? `<div class="message-file">Файл: <a href="${message.fileUrl}">${extractFileName(message.fileUrl)}</a></div>` : ''}
            </div>
        `).join('');
    }
    
    // Пагинация
    function updatePagination(currentPage) {
        paginationContainer.innerHTML = `
            <button ${currentPage === 0 ? 'disabled' : ''} onclick="loadMessages(${currentPage - 1})">Назад</button>
            <span>Страница ${currentPage + 1}</span>
            <button onclick="loadMessages(${currentPage + 1})">Вперед</button>
        `;
    }
    
    // Обработка отправки формы - ИСПРАВЛЕННАЯ ЧАСТЬ
    async function handleFormSubmit(e) {
        e.preventDefault();
        
        const receiver = document.getElementById('receiver').value;
        const description = document.getElementById('description').value;
        const fileUrl = document.getElementById('fileUrl').value;
        
        // Ключевое исправление: правильная структура данных для отправки
        const messageData = {
            description: description,
            fileUrl: fileUrl || null,
            receiver: {
                login: receiver  // Отправляем логин получателя, а не ID
            },
            sender: {
                login: currentUser  // Отправляем логин отправителя
            }
        };
        
        console.log('Отправляемые данные:', JSON.stringify(messageData));
        
        try {
            const response = await fetch(`${API_BASE}/send`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify(messageData)
            });
            
            if (response.ok) {
                showFormMessage('Сообщение успешно отправлено!', 'success');
                messageForm.reset();
                loadMessages(currentPage);
            } else {
                const errorText = await response.text();
                console.error('Ошибка сервера:', response.status, errorText);
                showFormMessage(`Ошибка: ${response.status} - ${errorText}`, 'error');
            }
        } catch (error) {
            console.error('Ошибка при отправке сообщения:', error);
            showFormMessage('Не удалось отправить сообщение', 'error');
        }
    }
    
    // Показать сообщение о результате отправки
    function showFormMessage(message, type) {
        formMessage.textContent = message;
        formMessage.className = type;
        formMessage.style.display = 'block';
        
        setTimeout(() => {
            formMessage.style.display = 'none';
        }, 5000);
    }
    
    // Вспомогательные функции
    /*function formatDate(dateString) {
        if (!dateString) return 'Дата неизвестна';
        return new Date(dateString).toLocaleString('ru-RU');
    }*/
    
    function extractFileName(fileUrl) {
        if (!fileUrl) return '';
        return fileUrl.split('/').pop() || fileUrl;
    }
    
    // Делаем функцию глобальной для пагинации
    window.loadMessages = loadMessages;
    
    // Запускаем приложение
    init();
});