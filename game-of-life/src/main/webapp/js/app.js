/**
 * Game of Life 前端應用程式
 */

const API_BASE = '/api/game';

let autoTickInterval = null;
let generation = 0;

// 初始化
document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('newGame').addEventListener('click', createGame);
    document.getElementById('nextTick').addEventListener('click', tick);
    document.getElementById('autoTick').addEventListener('change', toggleAutoTick);

    // 啟動時建立預設遊戲
    createGame();
});

/**
 * 建立新遊戲
 */
async function createGame() {
    const width = parseInt(document.getElementById('width').value);
    const height = parseInt(document.getElementById('height').value);

    // 產生隨機初始狀態（約 30% 活細胞）
    const state = generateRandomState(width, height);

    try {
        const response = await fetch(API_BASE, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ width, height, state })
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        generation = 0;
        updateGeneration();
        renderBoard(data);
    } catch (error) {
        console.error('建立遊戲失敗:', error);
        alert('無法連線到後端伺服器，請確認後端已啟動。');
    }
}

/**
 * 執行一代演化
 */
async function tick() {
    try {
        const response = await fetch(`${API_BASE}/tick`, { method: 'POST' });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        generation++;
        updateGeneration();
        renderBoard(data);
    } catch (error) {
        console.error('執行 tick 失敗:', error);
        // 發生錯誤時停止自動執行
        if (autoTickInterval) {
            document.getElementById('autoTick').checked = false;
            toggleAutoTick({ target: { checked: false } });
        }
    }
}

/**
 * 切換自動執行
 */
function toggleAutoTick(event) {
    if (event.target.checked) {
        autoTickInterval = setInterval(tick, 100); // 0.1 秒
    } else {
        if (autoTickInterval) {
            clearInterval(autoTickInterval);
            autoTickInterval = null;
        }
    }
}

/**
 * 渲染棋盤
 */
function renderBoard(data) {
    const table = document.getElementById('board');
    table.innerHTML = '';

    const lines = data.board.split('\n');
    lines.forEach((line, y) => {
        const tr = document.createElement('tr');
        [...line].forEach((cell, x) => {
            const td = document.createElement('td');
            td.className = cell === 'X' ? 'alive' : 'dead';
            td.dataset.x = x;
            td.dataset.y = y;
            tr.appendChild(td);
        });
        table.appendChild(tr);
    });
}

/**
 * 更新世代顯示
 */
function updateGeneration() {
    document.getElementById('generation').textContent = generation;
}

/**
 * 產生隨機狀態
 */
function generateRandomState(width, height) {
    return Array(width * height)
        .fill(0)
        .map(() => Math.random() > 0.7 ? 'X' : '.')
        .join('');
}
