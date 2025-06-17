import React from 'react';
import { Link, Outlet } from 'react-router';
import '@styles/AdminLayout.css';


export const AdminLayout: React.FC = () => {
  return (
    <div className="admin-layout">
  <aside className="admin-sidebar">
    <h2 className="sidebar-title">Quản lý</h2>
    <nav>
      <ul>
        <li><Link to="/admin/course">📚 Khóa học</Link></li>
        <li><Link to="/admin/users">👤 Người dùng</Link></li>
        <li><Link to="/admin/carts">🛒 Đơn hàng</Link></li>
        <li><Link to="/admin/roles">🔐 Phân quyền</Link></li>
      </ul>
    </nav>
  </aside>
  <main className="admin-content">
    <Outlet />
  </main>
</div>

  );
};