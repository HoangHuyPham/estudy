import React, { useEffect, useState } from 'react';
import axios from 'axios';

interface Course {
  id: string;
  name: string;
  description: string;
  image: string;
  price: number;
}

export const ProductManagement: React.FC = () => {
  const [courses, setCourses] = useState<Course[]>([]);
  const [newCourse, setNewCourse] = useState<Partial<Course>>({});
  const token = localStorage.getItem('token');

  useEffect(() => {
    axios.get('http://localhost:8080/api/admin/course', {
      headers: { Authorization: `Bearer ${token}` }
    }).then(res => setCourses(res.data));
  }, []);

  const handleCreate = () => {
    axios.post('http://localhost:8080/api/admin/course', newCourse, {
      headers: { Authorization: `Bearer ${token}` }
    }).then(() => window.location.reload());
  };

  const handleDelete = (id: string) => {
    axios.delete(`http://localhost:8080/api/admin/course/${id}`, {
      headers: { Authorization: `Bearer ${token}` }
    }).then(() => window.location.reload());
  };

  return (
    <div>
      <h2>Quản lý khóa học</h2>
      <input placeholder="Tên" onChange={e => setNewCourse({...newCourse, name: e.target.value})} />
      <input placeholder="Mô tả" onChange={e => setNewCourse({...newCourse, description: e.target.value})} />
      <input placeholder="Ảnh" onChange={e => setNewCourse({...newCourse, image: e.target.value})} />
      <input placeholder="Giá" type="number" onChange={e => setNewCourse({...newCourse, price: Number(e.target.value)})} />
      <button onClick={handleCreate}>Thêm khóa học</button>

      <ul>
        {courses.map(course => (
          <li key={course.id}>
            {course.name} - {course.price}đ
            <button onClick={() => handleDelete(course.id)}>Xóa</button>
          </li>
        ))}
      </ul>
    </div>
  );
};
