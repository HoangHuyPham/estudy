import { Button, Form, Input, message, Modal, Space, Spin, Table } from "antd";
import { useEffect, useState } from "react";
import { Endpoint, AppRequest } from "@requests/index";
import { IRole } from "../../interfaces";
import { LoadingOutlined } from "@ant-design/icons";

const RoleManagement: React.FC = () => {
  const request = AppRequest.getInstance();

  const [roles, setRoles] = useState<IRole[]>([]);
  const [loading, setLoading] = useState(false);
  const [pagination, setPagination] = useState({ page: 0, size: 10, total: 0 });

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [currentRole, setCurrentRole] = useState<IRole | null>(null);
  const [form] = Form.useForm();

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

  useEffect(() => {
    fetchRoles();
  }, [pagination.page]);

  const fetchRoles = async () => {
    setLoading(true);
    try {
      const res = await request.get(`${Endpoint.ROLE_MAN_URL}?page=${pagination.page}&size=${pagination.size}`);
      if (res.status === 200) {
        setRoles(res.data.content);
        setPagination({ ...pagination, total: res.data.totalElements });
      } else {
        message.error("Failed to fetch roles");
      }
    } catch (err) {
      message.error("Error fetching roles");
    } finally {
      setLoading(false);
    }
  };

  const openModal = (role?: IRole) => {
    setCurrentRole(role || null);
    form.setFieldsValue(role || { name: "", description: "" });
    setIsModalOpen(true);
  };

  const handleSubmit = async () => {
    try {
      const values = await form.validateFields();

      const res = currentRole
        ? await request.put(`${Endpoint.ROLE_MAN_URL}/${currentRole.id}`, values)
        : await request.post(Endpoint.ROLE_MAN_URL, values);

      if (res.status === 200) {
        message.success(currentRole ? "Updated successfully" : "Created successfully");
        setIsModalOpen(false);
        fetchRoles();
      } else {
        message.error("Operation failed");
      }
    } catch (err) {
      console.error(err);
    }
  };

  const handleDelete = async (role: IRole) => {
    try {
      const res = await request.delete(`${Endpoint.ROLE_MAN_URL}/${role.id}`);
      if (res.status === 200) {
        message.success("Deleted successfully");
        fetchRoles();
      } else {
        message.error("Delete failed");
      }
    } catch (err) {
      message.error("Error deleting role");
    }
  };

  const columns = [
    { title: "ID", dataIndex: "id" },
    { title: "Name", dataIndex: "name" },
    { title: "Description", dataIndex: "description" },
    {
      title: "Actions",
      render: (_: any, record: IRole) => (
        <Space>
          <Button onClick={() => openModal(record)}>Edit</Button>
          <Button danger onClick={() => handleDelete(record)}>
            Delete
          </Button>
        </Space>
      ),
    },
  ];

  return (
    <div className="w-full p-4">
      <h2 className="text-xl font-semibold text-center mb-4">Role Management</h2>
      <div className="mb-4 text-right">
        <Button type="primary" onClick={() => openModal()}>
          Add Role
        </Button>
      </div>

      <Spin spinning={loading} indicator={<LoadingOutlined />}>
        <Table
          rowKey="id"
          dataSource={roles}
          columns={columns}
          pagination={{
            current: pagination.page + 1,
            pageSize: pagination.size,
            total: pagination.total,
            onChange: (page) => setPagination({ ...pagination, page: page - 1 }),
          }}
        />
      </Spin>

      <Modal
        open={isModalOpen}
        title={currentRole ? "Edit Role" : "Add Role"}
        onCancel={() => setIsModalOpen(false)}
        onOk={handleSubmit}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="name" label="Name" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item name="description" label="Description">
            <Input.TextArea rows={3} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default RoleManagement;
