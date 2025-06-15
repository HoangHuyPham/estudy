import React, { useState } from 'react';
import '../styles/Login.css';
import { useNavigate } from 'react-router';
import axiosClient from '../api/axiosClient';
import {AxiosError} from "axios"; // dùng client đã config

interface Props {
    onClose: () => void;
}

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
            const response = await axiosClient.post('/auth/login', {
                username,
                password,
            });

            const token = response.data?.data?.token;
            if (!token) {
                throw new Error('Không nhận được token từ phản hồi');
            }

            localStorage.setItem('token', token);
            setMessage('Đăng nhập thành công!');
            onClose();
            navigate('/home');
        } catch (err) {
            const error = err as AxiosError<{ message: string }>;
            const errorMsg =
                error.response?.data?.message || 'Đăng nhập thất bại hoặc sai thông tin!';
            setError(errorMsg);
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
