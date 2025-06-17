import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, message, Pagination } from 'antd';
import { AppRequest, Endpoint } from '@requests';
import axios from 'axios';

interface Course {
  id?: string;
  name: string;
  description: string;
  preview: Preview;
  price: number;
}

interface Preview {
  id?:string;
  url?: string;
  name?:string;
}

export const CourseManagement: React.FC = () => {
  const [courses, setCourses] = useState<Course[]>([]);
  const [pagination, setPagination] = useState({ page: 0, limit: 10, total: 0 });
  const [loading, setLoading] = useState(false);
  const [editingCourse, setEditingCourse] = useState<Course | null>(null);
  const [modalVisible, setModalVisible] = useState(false); // ✅ new
  const [form] = Form.useForm();

  const fetchCourses = async (page = 0, limit = 10) => {
    try {
      setLoading(true);
      const res = await AppRequest.getInstance().get(`${"http://localhost:8080/api/admin/course"}?page=${page}&limit=${limit}`);
      const { data, totalElements } = res.data;
      setCourses(data);
      setPagination({ page, limit, total: totalElements });
    } catch (err) {
      message.error('Không thể tải danh sách khóa học');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCourses();
  }, []);

  const handleDelete = async (id?: string) => {
    try {
      await AppRequest.getInstance().delete(`${"http://localhost:8080/api/admin/course"}/${id}`);
      message.success('Xóa khóa học thành công');
      fetchCourses(pagination.page, pagination.limit);
    } catch {
      message.error('Xóa thất bại');
    }
  };

  const handleEdit = (course: Course) => {
    setEditingCourse(course);
    form.setFieldsValue(course);
    setModalVisible(true); // ✅ show modal when editing
  };

  const handleCreate = () => {
    setEditingCourse(null);
    form.resetFields();
    setModalVisible(true); // ✅ show modal when creating
  };

  const handleSubmit = async (values: Course) => {
    try {
      if (editingCourse?.id) {
        await AppRequest.getInstance().put(`${"http://localhost:8080/api/admin/course"}/${editingCourse.id}`, values);
        message.success('Cập nhật thành công');
      } else {
        await AppRequest.getInstance().post(Endpoint.COURSE_MAN_URL, values);
        message.success('Tạo mới thành công');
      }
      fetchCourses(pagination.page, pagination.limit);
      setEditingCourse(null);
      form.resetFields();
      setModalVisible(false); // ✅ close modal
    } catch {
      message.error('Lưu khóa học thất bại');
    }
  };

  const columns = [
    {
      title: 'Tên khóa học',
      dataIndex: 'name',
    },
    {
      title: 'Mô tả',
      dataIndex: 'description',
    },
    {
      title: 'Ảnh',
      dataIndex: 'image',
      render: (text: string, record: Course) => <img src={record?.preview?.url} alt="Course" style={{ width: 100 }} />,
    },
    {
      title: 'Giá',
      dataIndex: 'price',
      render: (value: number) => `${value} đ`,
    },
    {
      title: 'Hành động',
      render: (_: any, record: Course) => (
        <>
          <Button onClick={() => handleEdit(record)} type="link">Sửa</Button>
          <Button onClick={() => handleDelete(record.id)} type="link" danger>Xóa</Button>
        </>
      )
    }
  ];

  return (
    <div>
      <h2 className="text-xl font-bold text-center mb-4">Quản lý khóa học</h2>
      <div style={{ display: 'flex', justifyContent: 'flex-end', marginBottom: 16 }}>
        <Button onClick={handleCreate} type="primary">
          Tạo mới
        </Button>
      </div>

      <Table
        dataSource={courses}
        columns={columns}
        rowKey="id"
        loading={loading}
        pagination={false}
      />

      <Pagination
        current={pagination.page + 1}
        pagelimit={pagination.limit}
        total={pagination.total}
        onChange={(page) => fetchCourses(page - 1, pagination.limit)}
        style={{ marginTop: 16 }}
      />

      <Modal
        open={modalVisible}
        title={editingCourse ? 'Cập nhật khóa học' : 'Tạo khóa học'}
        onCancel={() => setModalVisible(false)}
        onOk={() => form.submit()}
        destroyOnClose
      >
        <Form form={form} layout="vertical" onFinish={handleSubmit}>
          <Form.Item
            name="name"
            label="Tên khóa học"
            rules={[{ required: true, message: 'Vui lòng nhập tên khóa học' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="description"
            label="Mô tả"
            rules={[{ required: true, message: 'Vui lòng nhập mô tả' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="image"
            label="Link ảnh"
            rules={[{ required: true, message: 'Vui lòng nhập link ảnh' }]}
          >
            <Input />
          </Form.Item>

          <Form.Item
            name="price"
            label="Giá"
            rules={[{ required: true, message: 'Vui lòng nhập giá' }]}
          >
            <InputNumber min={0} style={{ width: '100%' }} />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};
