import React, { useState } from 'react';
import '../styles/Login.css';
import { useNavigate } from 'react-router';

interface Props {
    onClose: () => void;
}

const API_BASE = 'http://localhost:8080/api/auth';

export const Login: React.FC<Props> = ({ onClose }) => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setMessage('');

        try {
            const response = await fetch(`${API_BASE}/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Đăng nhập thất bại');
            }

            const token = data.data?.token;
            if (!token) {
                throw new Error('Không nhận được token từ phản hồi');
            }

            localStorage.setItem('token', token);
            setMessage('Đăng nhập thành công!');
            onClose();
            navigate('/home');
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Lỗi không xác định');
        }
    };

    return (
        <div className="auth-form login-form">
            <h2>Đăng nhập</h2>
            <form onSubmit={handleLogin}>
                {error && <p className="error">{error}</p>}
                {message && <p className="success">{message}</p>}

                <div>
                    <label>Tên đăng nhập:</label>
                    <input
                        type="text"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Mật khẩu:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Đăng nhập</button>
            </form>
        </div>
    );
};
