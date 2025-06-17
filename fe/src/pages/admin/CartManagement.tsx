import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input, message, Pagination } from 'antd';
import axios from 'axios';

interface User {
  username: string;
}

interface Cart {
  id?: string;
  user: User;
  total: number;
  createdAt?: string;
  cartItems?: any[];
}

const API = 'http://localhost:8080/api/admin/carts';

export const CartManagement: React.FC = () => {
  const [carts, setCarts] = useState<Cart[]>([]);
  const [pagination, setPagination] = useState({ page: 0, limit: 10, total: 0 });
  const [loading, setLoading] = useState(false);
  const [editingCart, setEditingCart] = useState<Cart | null>(null);
  const [modalVisible, setModalVisible] = useState(false);
  const [form] = Form.useForm();
  const token = localStorage.getItem('token');

  const fetchCarts = async (page = 0, limit = 10) => {
    try {
      setLoading(true);
      const res = await axios.get(`${API}?page=${page}&limit=${limit}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      const { data, total } = res.data;
      setCarts(data);
      setPagination({ page, limit, total });
    } catch (err) {
      message.error('Không thể tải danh sách giỏ hàng');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCarts();
  }, []);

  const handleDelete = async (id?: string) => {
    try {
      await axios.delete(`${API}/${id}`, {
        headers: { Authorization: `Bearer ${token}` }
      });
      message.success('Xóa thành công');
      fetchCarts(pagination.page, pagination.limit);
    } catch {
      message.error('Xóa thất bại');
    }
  };

  const handleEdit = (cart: Cart) => {
    setEditingCart(cart);
    form.setFieldsValue(cart);
    setModalVisible(true);
  };

  const handleCreate = () => {
    setEditingCart(null);
    form.resetFields();
    setModalVisible(true);
  };

  const handleSubmit = async (values: Cart) => {
    try {
      if (editingCart?.id) {
        await axios.put(`${API}/${editingCart.id}`, values, {
          headers: { Authorization: `Bearer ${token}` }
        });
        message.success('Cập nhật thành công');
      } else {
        await axios.post(API, values, {
          headers: { Authorization: `Bearer ${token}` }
        });
        message.success('Tạo mới thành công');
      }
      fetchCarts(pagination.page, pagination.limit);
      form.resetFields();
      setEditingCart(null);
      setModalVisible(false);
    } catch (err) {
      message.error('Lưu thất bại');
    }
  };

  const columns = [
    {
      title: 'Người dùng',
      dataIndex: ['user', 'username']
    },
    {
      title: 'Tổng tiền',
      dataIndex: 'total'
    },
    {
      title: 'Ngày tạo',
      dataIndex: 'createdAt',
      render: (text: string) => new Date(text).toLocaleString()
    },
    {
      title: 'Hành động',
      render: (_: any, record: Cart) => (
        <>
          <Button onClick={() => handleEdit(record)} type="link">Sửa</Button>
          <Button onClick={() => handleDelete(record.id)} type="link" danger>Xóa</Button>
        </>
      )
    }
  ];

  return (
    <div>
      <h2 className="text-xl font-bold text-center mb-4">Quản lý giỏ hàng</h2>
      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 16 }}>
        <Button onClick={handleCreate} type="primary" style={{ marginBottom: 16 }}>
          Tạo mới
        </Button>
      </div>
      <Table
        dataSource={carts}
        columns={columns}
        rowKey="id"
        pagination={false}
        loading={loading}
      />
      <Pagination
        current={pagination.page + 1}
        pageSize={pagination.limit}
        total={pagination.total}
        onChange={(page) => fetchCarts(page - 1, pagination.limit)}
        style={{ marginTop: 16 }}
      />

      <Modal
        open={modalVisible}
        title={editingCart ? 'Cập nhật giỏ hàng' : 'Tạo giỏ hàng'}
        onCancel={() => setModalVisible(false)}
        onOk={() => form.submit()}
        destroyOnClose
      >
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Form.Item
            name={['user', 'username']}
            label="Tên người dùng"
            rules={[{ required: true, message: 'Bắt buộc nhập tên người dùng' }]}
          >
            <Input />
          </Form.Item>
          <Form.Item
            name="total"
            label="Tổng tiền"
            rules={[{ required: true, message: 'Bắt buộc nhập tổng tiền' }]}
          >
            <Input type="number" />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};
