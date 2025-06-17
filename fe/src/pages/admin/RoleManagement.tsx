import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Button, Select, Table, message, Spin, Card } from 'antd';
import { LoadingOutlined } from '@ant-design/icons';

interface User {
  id: string;
  username: string;
  role: { name: string };
}

export const RoleManagement: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(false);
  const [updating, setUpdating] = useState<string | null>(null);
  const token = localStorage.getItem('token');

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    if (!token) {
      message.error('Không tìm thấy token xác thực');
      return;
    }

    setLoading(true);
    try {
      const response = await axios.get('http://localhost:8080/api/admin/users?page=0&limit=10', {
        headers: { Authorization: `Bearer ${token}` }
      });
      setUsers(response.data.data);
    } catch (error) {
      console.error('Error fetching users:', error);
      message.error('Không thể tải danh sách người dùng');
    } finally {
      setLoading(false);
    }
  };

  const changeRole = async (userId: string, newRole: string) => {
    if (!token) {
      message.error('Không tìm thấy token xác thực');
      return;
    }

    setUpdating(userId);
    try {
      await axios.put(
        `http://localhost:8080/api/admin/users/${userId}/role?role=${newRole}`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` }
        }
      );

      message.success('Đổi quyền thành công!');
      setUsers(prevUsers =>
        prevUsers.map(user =>
          user.id === userId
            ? { ...user, role: { name: newRole } }
            : user
        )
      );
    } catch (error) {
      console.error('Error changing role:', error);
      message.error('Không thể đổi quyền người dùng');
    } finally {
      setUpdating(null);
    }
  };

  const columns = [
    {
      title: 'ID',
      dataIndex: 'id',
      key: 'id',
      width: '30%',
    },
    {
      title: 'Tên người dùng',
      dataIndex: 'username',
      key: 'username',
      width: '25%',
    },
    {
      title: 'Quyền hiện tại',
      dataIndex: ['role', 'name'],
      key: 'role',
      width: '20%',
      render: (roleName: string) => (
        <span className={`px-2 py-1 rounded text-xs font-medium ${
          roleName === 'ADMIN'
            ? 'bg-red-100 text-red-800'
            : 'bg-blue-100 text-blue-800'
        }`}>
          {roleName}
        </span>
      ),
    },
    {
      title: 'Thao tác',
      key: 'action',
      width: '25%',
      render: (_: any, record: User) => (
        <div className="flex gap-2">
          <Select
            defaultValue={record.role?.name}
            style={{ width: 100 }}
            onChange={(value) => changeRole(record.id, value)}
            loading={updating === record.id}
            disabled={updating === record.id}
            options={[
              { value: 'USER', label: 'USER' },
              { value: 'ADMIN', label: 'ADMIN' },
            ]}
          />
        </div>
      ),
    },
  ];

  return (
    <div className="p-6">
      <Card>
        <div className="mb-4">
          <h2 className="text-2xl font-bold mb-2">Quản lý phân quyền người dùng</h2>
          <p className="text-gray-600">
            Quản lý quyền truy cập của người dùng trong hệ thống
          </p>
        </div>

        <Spin spinning={loading} indicator={<LoadingOutlined />}>
          <Table
            dataSource={users}
            columns={columns}
            rowKey="id"
            pagination={{
              pageSize: 10,
              showSizeChanger: true,
              showQuickJumper: true,
              showTotal: (total, range) =>
                `${range[0]}-${range[1]} của ${total} người dùng`,
            }}
            locale={{
              emptyText: 'Không có dữ liệu',
            }}
          />
        </Spin>

        <div className="mt-4 flex justify-end">
          <Button
            onClick={fetchUsers}
            loading={loading}
          >
            Làm mới dữ liệu
          </Button>
        </div>
      </Card>
    </div>
  );
};