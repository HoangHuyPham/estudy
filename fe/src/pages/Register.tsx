import React, {useState} from 'react';
import '../styles/Register.css';

interface Props {
    onClose: () => void;
}

const API_BASE = 'http://localhost:8080/api/auth';

export const Register: React.FC<Props> = ({ onClose }) => {
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
            const response = await fetch(`${API_BASE}/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            const data = await response.json();
            if (!response.ok) throw new Error(data.message || 'Đăng ký thất bại');

            setMessage('Đăng ký thành công! Bạn có thể đăng nhập.');
            onClose();
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Lỗi không xác định');
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