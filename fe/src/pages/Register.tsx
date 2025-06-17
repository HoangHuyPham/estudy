import React, { useState, useEffect } from 'react';
import '../styles/Register.css';
import { AppRequest } from '@requests';

export const Register: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');

    const handleRegister = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setMessage('');

        if (password !== confirmPassword) {
            setError('Mật khẩu xác nhận không khớp.');
            return;
        }

        try {
            const response = await AppRequest.getInstance().post('/api/auth/register', {
                username,
                password,
            });

            setMessage(response.data?.message || 'Đăng ký thành công!');
            onClose();
            } catch (error: any) {
            if (error.response && error.response.data?.message) {
                setError(error.response.data.message);
            } else {
                // setError('Lỗi đăng ký. Vui lòng thử lại.');
            }
        }
    };

    return (
        <div className="auth-form register-form">
            <h2>Đăng ký</h2>
            <form onSubmit={handleRegister}>
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
                <div>
                    <label>Xác nhận mật khẩu:</label>
                    <input
                        type="password"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Đăng ký</button>
            </form>
        </div>
    );
};
