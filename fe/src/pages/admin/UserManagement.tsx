import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface User {
  id: string;
  username: string;
  role: { name: string };
}

export const UserManagement: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const token = localStorage.getItem('token');

  useEffect(() => {
    axios.get('http://localhost:8080/api/admin/users', {
      headers: { Authorization: `Bearer ${token}` }
    }).then(res => setUsers(res.data));
  }, []);

  const handleDelete = (id: string) => {
    axios.delete(`http://localhost:8080/api/admin/users/${id}`, {
      headers: { Authorization: `Bearer ${token}` }
    }).then(() => window.location.reload());
  };

  return (
    <div>
      <h2>Quản lý người dùng</h2>
      <ul>
        {users.map(u => (
          <li key={u.id}>
            {u.username} - {u.role?.name}
            <button onClick={() => handleDelete(u.id)}>Xóa</button>
          </li>
        ))}
      </ul>
    </div>
  );
};
