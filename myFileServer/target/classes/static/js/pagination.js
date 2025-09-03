// Конфигурация
let currentPage = 0;
let pageSize = 10;
let totalPages = 1;
let totalElements = 0;

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', function() {
    initializePagination();
    loadFiles(currentPage, pageSize);
});

// Инициализация обработчиков событий
function initializePagination() {
    document.getElementById('prevBtn').addEventListener('click', function() {
        changePage(currentPage - 1);
    });

    document.getElementById('nextBtn').addEventListener('click', function() {
        changePage(currentPage + 1);
    });
}

// Загрузка файлов с сервера
async function loadFiles(page, size) {
    showLoading();

    try {
        // ИСПРАВЛЕННЫЙ URL - без /api/files
        const response = await fetch(`/getUploaded?page=${page}&size=${size}`);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const files = await response.json();
        updateFilesTable(files);
        updatePaginationInfo();

    } catch (error) {
        console.error('Error loading files:', error);
        showError('Error loading files: ' + error.message);

        // Покажем тестовые данные для демонстрации
        showTestData();
    }
}

// Показать состояние загрузки
function showLoading() {
    document.getElementById('filesBody').innerHTML = `
        <tr>
            <td colspan="3" class="loading">Loading files...</td>
        </tr>
    `;
}

// Показать ошибку
function showError(message) {
    document.getElementById('filesBody').innerHTML = `
        <tr>
            <td colspan="3" class="error">${message}</td>
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
                <td colspan="3" class="empty">No files found</td>
            </tr>
        `;
        return;
    }

    tbody.innerHTML = files.map(file => `
        <tr>
            <td>${escapeHtml(file.name)}</td>
            <td>${escapeHtml(file.size)}</td>
            <td>${formatDate(file.date)}</td>
        </tr>
    `).join('');
}

// Обновление информации о пагинации
function updatePaginationInfo() {
    const pageInfo = document.getElementById('pageInfo');
    const paginationInfo = document.getElementById('paginationInfo');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages}`;

    const startFile = currentPage * pageSize + 1;
    const endFile = Math.min((currentPage + 1) * pageSize, totalElements);
    paginationInfo.textContent = `Showing ${startFile}-${endFile} of ${totalElements} files`;

    prevBtn.disabled = currentPage === 0;
    nextBtn.disabled = currentPage >= totalPages - 1;
}

// Смена страницы
function changePage(newPage) {
    if (newPage >= 0 && newPage < totalPages) {
        currentPage = newPage;
        loadFiles(currentPage, pageSize);
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