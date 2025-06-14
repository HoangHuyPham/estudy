import React, { useState } from 'react';
import '../styles/Home.css';
import { Login } from '../pages/Login.tsx';
import { Register } from '../pages/Register.tsx';

// const API_BASE = 'http://localhost:8080/api/auth';


export const Home: React.FC = () => {
    const [showLogin, setShowLogin] = useState(false);
    const [showRegister, setShowRegister] = useState(false);
    // const [username, setUsername] = useState('');
    // const [password, setPassword] = useState('');
    // const [confirmPassword, setConfirmPassword] = useState('');
    // const [error, setError] = useState('');
    // const [message, setMessage] = useState('');
    //
    // const resetForm = () => {
    //     setUsername('');
    //     setPassword('');
    //     setConfirmPassword('');
    //     setError('');
    //     setMessage('');
    // };

    // const handleLogin = async (e: React.FormEvent) => {
    //     e.preventDefault();
    //     setError('');
    //     setMessage('');
    //     try {
    //         const response = await fetch(`${API_BASE}/login`, {
    //             method: 'POST',
    //             headers: { 'Content-Type': 'application/json' },
    //             body: JSON.stringify({ username, password }),
    //         });
    //
    //         const data = await response.json();
    //         if (!response.ok) throw new Error(data.message || 'Đăng nhập thất bại');
    //
    //         localStorage.setItem('token', data.token);
    //         setMessage('Đăng nhập thành công!');
    //         resetForm();
    //         setShowLogin(false);
    //     } catch (err: unknown) {
    //         if (err instanceof Error) {
    //             setError(err.message);
    //         } else {
    //             setError('Lỗi không xác định');
    //         }
    //     }
    // };

    // const handleRegister = async (e: React.FormEvent) => {
    //     e.preventDefault();
    //     setError('');
    //     setMessage('');
    //
    //     if (password !== confirmPassword) {
    //         setError('Mật khẩu xác nhận không khớp.');
    //         return;
    //     }
    //
    //     try {
    //         const response = await fetch(`${API_BASE}/register`, {
    //             method: 'POST',
    //             headers: { 'Content-Type': 'application/json' },
    //             body: JSON.stringify({ username, password }),
    //         });
    //
    //         const data = await response.json();
    //         if (!response.ok) throw new Error(data.message || 'Đăng ký thất bại');
    //
    //         setMessage('Đăng ký thành công! Bạn có thể đăng nhập.');
    //         resetForm();
    //         setShowRegister(false);
    //     } catch (err: unknown) {
    //         if (err instanceof Error) {
    //             setError(err.message);
    //         } else {
    //             setError('Lỗi không xác định');
    //         }
    //     }
    // };

    return (
        <div className="home-page">
            <header className="home-header">
                <div className="header-left">
                    <h1>Học Online</h1>
                    <p>Nền tảng học trực tuyến chất lượng – từ lập trình đến kỹ năng mềm.</p>
                </div>
                <div className="header-right">
                    <button onClick={() => {
                        setShowLogin(!showLogin);
                        setShowRegister(false);
                    }}>Đăng nhập</button>

                    <button onClick={() => {
                        setShowRegister(!showRegister);
                        setShowLogin(false);
                    }}>Đăng ký</button>
                </div>
            </header>

            {showLogin && <Login onClose={() => setShowLogin(false)} />}
            {showRegister && <Register onClose={() => setShowRegister(false)} />}

            <section className="featured-courses">
                <h2>Khóa học nổi bật</h2>
                <div className="course-list">
                    <div className="course-card">
                        <h3>Lập trình React</h3>
                        <p>Học cách xây dựng ứng dụng web hiện đại với ReactJS.</p>
                        <button>Xem chi tiết</button>
                    </div>
                    <div className="course-card">
                        <h3>Python cho người mới bắt đầu</h3>
                        <p>Khóa học nền tảng giúp bạn làm quen với Python.</p>
                        <button>Xem chi tiết</button>
                    </div>
                </div>
            </section>

            <section className="why-choose-us">
                <h2>Tại sao chọn chúng tôi?</h2>
                <ul>
                    <li>Giảng viên chất lượng và giàu kinh nghiệm</li>
                    <li>Nội dung khóa học được cập nhật liên tục</li>
                    <li>Học mọi lúc mọi nơi, trên mọi thiết bị</li>
                </ul>
            </section>
        </div>
    );
};
