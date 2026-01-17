#!/usr/bin/env python3
"""
Game of Life 前端代理伺服器

功能：
1. 提供靜態檔案服務 (HTML/CSS/JS)
2. 將 /api/* 請求代理到 Spring Boot 後端

使用方式：
    python3 proxy_server.py
"""

import http.server
import socketserver
import urllib.request
import urllib.error
from pathlib import Path

# 設定
FRONTEND_PORT = 3000
BACKEND_URL = "http://localhost:8080"
WEBAPP_DIR = Path(__file__).parent.parent / "src" / "main" / "webapp"


class ProxyHandler(http.server.SimpleHTTPRequestHandler):
    """處理靜態檔案和 API 代理的 HTTP Handler"""

    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=str(WEBAPP_DIR), **kwargs)

    def do_GET(self):
        """處理 GET 請求"""
        if self.path.startswith('/api/'):
            self._proxy_request('GET')
        else:
            super().do_GET()

    def do_POST(self):
        """處理 POST 請求"""
        if self.path.startswith('/api/'):
            self._proxy_request('POST')
        else:
            self.send_error(405, "Method Not Allowed")

    def do_OPTIONS(self):
        """處理 CORS preflight 請求"""
        self.send_response(200)
        self.send_header('Access-Control-Allow-Origin', '*')
        self.send_header('Access-Control-Allow-Methods', 'GET, POST, OPTIONS')
        self.send_header('Access-Control-Allow-Headers', 'Content-Type')
        self.end_headers()

    def _proxy_request(self, method):
        """代理請求到後端"""
        url = f"{BACKEND_URL}{self.path}"

        # 讀取請求體
        content_length = int(self.headers.get('Content-Length', 0))
        body = self.rfile.read(content_length) if content_length > 0 else None

        # 建立代理請求
        headers = {'Content-Type': 'application/json'} if body else {}
        req = urllib.request.Request(
            url,
            data=body,
            method=method,
            headers=headers
        )

        try:
            with urllib.request.urlopen(req, timeout=10) as response:
                self.send_response(response.status)
                self.send_header('Content-Type', 'application/json')
                self.send_header('Access-Control-Allow-Origin', '*')
                self.end_headers()
                self.wfile.write(response.read())
        except urllib.error.HTTPError as e:
            self.send_response(e.code)
            self.send_header('Content-Type', 'application/json')
            self.end_headers()
            self.wfile.write(e.read())
        except urllib.error.URLError as e:
            self.send_error(502, f"後端無法連線: {e.reason}")
        except Exception as e:
            self.send_error(500, f"內部錯誤: {str(e)}")

    def log_message(self, format, *args):
        """自訂日誌格式"""
        print(f"[{self.log_date_time_string()}] {args[0]}")


def main():
    """啟動伺服器"""
    print(f"=" * 50)
    print(f"Game of Life 前端代理伺服器")
    print(f"=" * 50)
    print(f"前端目錄: {WEBAPP_DIR}")
    print(f"前端網址: http://localhost:{FRONTEND_PORT}")
    print(f"後端代理: {BACKEND_URL}")
    print(f"=" * 50)
    print(f"請確保後端已啟動：./mvnw spring-boot:run -pl game-of-life")
    print(f"按 Ctrl+C 停止伺服器")
    print(f"=" * 50)

    with socketserver.TCPServer(("", FRONTEND_PORT), ProxyHandler) as httpd:
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\n伺服器已停止")


if __name__ == "__main__":
    main()
