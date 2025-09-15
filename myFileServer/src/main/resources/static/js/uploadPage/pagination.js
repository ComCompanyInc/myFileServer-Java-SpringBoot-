// Конфигурация
let currentPage = 0;
let pageSize = 10;
let totalPages = 1;
let totalElements = 0;
let currentUserLogin = '';
let currentSearchTerm = ''; // Добавляем переменную для хранения текущего поискового запроса

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    initializePagination();
    initializeSearch();
    getCurrentLogin().then(() => {
        loadFiles(currentPage, pageSize, currentSearchTerm);
    });
});

// Инициализация поиска
function initializeSearch() {
    document.getElementById('searchBtn').addEventListener('click', function() {
        performSearch();
    });

    document.getElementById('clearSearchBtn').addEventListener('click', function() {
        clearSearch();
    });

    // Поиск при нажатии Enter в поле ввода
    document.getElementById('searchInput').addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            performSearch();
        }
    });
}

// Выполнение поиска
function performSearch() {
    currentSearchTerm = document.getElementById('searchInput').value.trim();
    currentPage = 0; // Сбрасываем на первую страницу при новом поиске
    loadFiles(currentPage, pageSize, currentSearchTerm);
}

// Очистка поиска
function clearSearch() {
    document.getElementById('searchInput').value = '';
    currentSearchTerm = '';
    currentPage = 0;
    loadFiles(currentPage, pageSize, currentSearchTerm);
}

// Получение логина текущего пользователя
async function getCurrentLogin() {
    try {
        const response = await fetch('/currentLogin');
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        currentUserLogin = await response.text();
        console.log('Current user login:', currentUserLogin);
    } catch (error) {
        console.error('Error getting user login:', error);
        currentUserLogin = '';
    }
}

// Инициализация обработчиков событий пагинации
function initializePagination() {
    document.getElementById('prevBtn').addEventListener('click', function() {
        changePage(currentPage - 1);
    });

    document.getElementById('nextBtn').addEventListener('click', function() {
        changePage(currentPage + 1);
    });
}

// Загрузка файлов с сервера
async function loadFiles(page, size, searchTerm) {
    showLoading();

    try {
        const response = await fetch(`/getUploaded?searchField=${encodeURIComponent(searchTerm)}&page=${page}&size=${size}`);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const files = await response.json();
        
        // ВРЕМЕННОЕ РЕШЕНИЕ: так как бэкенд возвращает только массив файлов
        // без метаданных пагинации, мы должны сами управлять состоянием
        
        // Предположим, что если файлов больше или равно pageSize, 
        // то probably есть еще страницы
        if (files.length >= pageSize) {
            totalPages = page + 2; // Предполагаем, что есть следующая страница
        } else {
            totalPages = page + 1; // Это последняя страница
        }
        
        totalElements = (page * size) + files.length;
        currentPage = page;
        
        updateFilesTable(files);
        updatePaginationInfo();

    } catch (error) {
        console.error('Error loading files:', error);
        showError('Error loading files: ' + error.message);
        showTestData();
    }
}

// Показать состояние загрузки
function showLoading() {
    document.getElementById('filesBody').innerHTML = `
        <tr>
            <td colspan="6" class="loading">Loading files...</td>
        </tr>
    `;
}

// Показать ошибку
function showError(message) {
    document.getElementById('filesBody').innerHTML = `
        <tr>
            <td colspan="6" class="error">${message}</td>
        </tr>
    `;
}

// Показать тестовые данные (если сервер не отвечает)
function showTestData() {
    const testData = [
        { name: "example.txt", size: "1.23 MB", date: new Date() },
        { name: "photo.jpg", size: "2.45 MB", date: new Date() },
        { name: "document.pdf", size: "0.87 MB", date: new Date() }
    ];

    updateFilesTable(testData);

    // Обновим информацию о пагинации
    totalElements = 3;
    totalPages = 1;
    updatePaginationInfo();
}

// Обновление таблицы с файлами
function updateFilesTable(files) {
    const tbody = document.getElementById('filesBody');

    if (!files || files.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="empty">Файлов не найдено</td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = files.map(file => `
        <tr>
            <td>${escapeHtml(file.name)}</td>
            <td>${escapeHtml(file.size)}</td>
            <td>${formatDate(file.date)}</td>
            <td>    
                <button type="button" class="download-btn" data-filename="${escapeHtml(file.name)}"> 
                    Скачать 
                </button>
            </td>
            <td>
                <button type="button" class="delete-btn" data-filename="${escapeHtml(file.name)}">
                   Удалить
                </button>
            </td>
            <td>
                <div class="copy-text">
                    /download?login=${encodeURIComponent(currentUserLogin)}&nameFile=${encodeURIComponent(file.name)}
                </div>
            </td>
        </tr>
    `).join('');
}

// Обновление информации о пагинации
function updatePaginationInfo() {
    const pageInfo = document.getElementById('pageInfo');
    const paginationInfo = document.getElementById('paginationInfo');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    pageInfo.textContent = `Страница ${currentPage + 1} из ${totalPages}`;

    const startFile = currentPage * pageSize + 1;
    const endFile = Math.min((currentPage + 1) * pageSize, totalElements);
    paginationInfo.textContent = `Просмотренно ${startFile}-${endFile} из ${totalElements} файлов`;

    prevBtn.disabled = currentPage === 0;
    nextBtn.disabled = currentPage >= totalPages - 1;
}

// Смена страницы
function changePage(newPage) {
    if (newPage >= 0 && newPage < totalPages) {
        currentPage = newPage;
        loadFiles(currentPage, pageSize, currentSearchTerm);
    }
}

// Вспомогательная функция для форматирования даты
function formatDate(dateString) {
    try {
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    } catch (e) {
        return 'Invalid date';
    }
}

// Вспомогательная функция для экранирования HTML
function escapeHtml(text) {
    if (!text) return '';
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Обработчик скачивания файла
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('download-btn')) {
        const fileName = e.target.getAttribute('data-filename');
        window.open(`/download?login=${encodeURIComponent(currentUserLogin)}&nameFile=${encodeURIComponent(fileName)}`, '_blank');
    }
});

// Обработчик удаления файла
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('delete-btn')) {
        const fileName = e.target.getAttribute('data-filename');
        
        if (confirm(`Удалить файл "${fileName}"?`)) {
            fetch('/delete?login=' + encodeURIComponent(currentUserLogin) + '&nameFile=' + encodeURIComponent(fileName), {
                method: 'DELETE'
            })
            .then(response => {
                if (response.ok) {
                    alert('Файл удален');
                    loadFiles(currentPage, pageSize, currentSearchTerm);
                } else {
                    throw new Error('Ошибка сервера');
                }
            })
            .catch(error => {
                console.error('Ошибка удаления:', error);
                alert('Ошибка при удалении файла');
            });
        }
    }
});