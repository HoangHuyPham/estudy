import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface User {
  id: string;
  username: string;
  role: { name: string };
}

export const RoleManagement: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [selectedRole, setSelectedRole] = useState<string>('USER');
  const token = localStorage.getItem('token');

  useEffect(() => {
    axios.get('http://localhost:8080/api/admin/users', {
      headers: { Authorization: `Bearer ${token}` }
    }).then(res => setUsers(res.data));
  }, []);

  const changeRole = (id: string) => {
    axios.put(`http://localhost:8080/api/admin/users/${id}/role?role=${selectedRole}`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    }).then(() => window.location.reload());
  };

  return (
    <div>
      <h2>Phân quyền người dùng</h2>
      <select onChange={e => setSelectedRole(e.target.value)}>
        <option value="USER">USER</option>
        <option value="ADMIN">ADMIN</option>
      </select>
      <ul>
        {users.map(u => (
          <li key={u.id}>
            {u.username} - {u.role?.name}
            <button onClick={() => changeRole(u.id)}>Đổi quyền</button>
          </li>
        ))}
      </ul>
    </div>
  );
};
