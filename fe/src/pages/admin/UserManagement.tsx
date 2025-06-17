import React, { useEffect, useState } from "react";
import { Button, Form, Input, Modal, Table, message } from "antd";
import { AppRequest, Endpoint } from "@requests";
import '@styles/UserManagement.css';
import { IRole } from "../../interfaces";

interface Role {
  id: string;
  name: string;
}

interface User {
  id: string;
  username: string;
  password?: string;
  role: Role;
}

export const UserManagement: React.FC = () => {
  const request = AppRequest.getInstance();
  const [users, setUsers] = useState<User[]>([]);
  const [newUser, setNewUser] = useState<Partial<User>>({});
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentUser, setCurrentUser] = useState<User | null>(null);
  const [form] = Form.useForm();
  const [pagination, setPagination] = useState({ page: 0, size: 10, total: 0 });

  const token = localStorage.getItem("token");

  useEffect(() => {
    fetchUsers();
  }, [pagination.page]);

  const fetchUsers = async () => {
    try {
      const res = await request.get(`${"http://localhost:8080/api/admin/users"}?page=${pagination.page}&limit=${pagination.size}`);
      if (res.status === 200) {
        setUsers(res.data.data);
        setPagination({ ...pagination, total: res.data.totalElements });
      } else {
        message.error("Lấy danh sách người dùng thất bại");
      }
    } catch (err) {
      message.error("Lỗi khi lấy danh sách người dùng");
    }
  };

  const handleDelete = async (user: User) => {
    try {
      const res = await request.delete(`${"http://localhost:8080/api/admin/users"}/${user.id}`);
      if (res.status === 200) {
        message.success("Xóa thành công");
        fetchUsers();
      } else {
        message.error("Xóa thất bại");
      }
    } catch (err) {
      message.error("Lỗi khi xóa người dùng");
    }
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      const res = currentUser
        ? await request.put(`${"http://localhost:8080/api/admin/users"}/${currentUser.id}`, values)
        : await request.post("http://localhost:8080/api/admin/users", values);

      if (res.status === 200) {
        message.success(currentUser ? "Cập nhật thành công" : "Tạo thành công");
        setIsModalOpen(false);
        fetchUsers();
      } else {
        message.error("Thao tác thất bại");
      }
    } catch (err) {
      console.error(err);
    }
  };

  const openModal = (user?: User) => {
    setCurrentUser(user || null);
    form.setFieldsValue(user || { username: "", password: "", role: { id: "", name: "" } });
    setIsModalOpen(true);
  };

  return (
    <div className="w-full p-4">
      <h2 className="text-xl font-bold text-center mb-4">Quản lý người dùng</h2>
      <div className="mb-4 text-right">
        <Button type="primary" onClick={() => openModal()}>
          Thêm người dùng
        </Button>
      </div>

      <Table
        rowKey="id"
        dataSource={users}
        columns={[
          { title: "ID", dataIndex: "id" },
          { title: "Username", dataIndex: "username" },
          {
            title: "Role",
            dataIndex: "role",
            render: (role: Role) => role?.name,
          },
          {
            title: "Actions",
            render: (_: any, record: User) => (
              <>
                <Button onClick={() => openModal(record)}>Sửa</Button>{" "}
                <Button danger onClick={() => handleDelete(record)}>
                  Xóa
                </Button>
              </>
            ),
          },
        ]}
        pagination={{
          current: pagination.page + 1,
          pageSize: pagination.size,
          total: pagination.total,
          onChange: (page) => setPagination({ ...pagination, page: page - 1 }),
        }}
      />

      <Modal
        open={isModalOpen}
        title={currentUser ? "Chỉnh sửa người dùng" : "Thêm người dùng"}
        onCancel={() => setIsModalOpen(false)}
        onOk={handleSubmit}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="username" label="Username" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          {!currentUser && (
            <Form.Item name="password" label="Password" rules={[{ required: true }]}>
              <Input.Password />
            </Form.Item>
          )}
          <Form.Item name={["role", "id"]} label="Role ID" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

