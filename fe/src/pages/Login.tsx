import React, { useState } from 'react';
import '@styles/Login.css';
import { useNavigate } from 'react-router';
import { AxiosError } from 'axios';
import { Endpoint, AppRequest } from '@requests';

export const Login: React.FC = () => {
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
      const response = await AppRequest.getInstance().post(Endpoint.LOGIN_URL, {
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
      navigate('/Home');
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
