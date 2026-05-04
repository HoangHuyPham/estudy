import { Navigate, Route, Routes } from 'react-router-dom'
import { AuthRoute } from '@/route/AuthRoute'
import { HomeLayout } from '@/layouts/HomeLayout'
import { HomePage } from '@/page/HomePage'
import { ManageCourseLayout } from '@/layouts/tutor/ManageCourseLayout'
import { ErrorPage } from '@/page/ErrorPage'
import { CoursesPage } from '@/page/protected/tutor/CoursesPage'
import { DraftsPage } from '@/page/protected/tutor/DraftsPage'
import { CoursePage } from '@/page/protected/tutor/CoursePage'

export const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/home" />} />

      <Route path="/home" element={<HomeLayout />}>
        <Route index element={<HomePage />} />
      </Route>

      <Route path="/tutor/manage-courses" element={<HomeLayout />}>
        <Route path="" element={<ManageCourseLayout />}>
          <Route index element={<Navigate to="courses" />} />
          <Route path="courses" element={<CoursesPage />} />
          <Route path="drafts" element={<DraftsPage />} />
        </Route>

        <Route path="course/:id" element={<CoursePage />} />
      </Route>

      <Route element={<AuthRoute />}>
        <Route path="/admin/*" element={null} />
      </Route>

      <Route path="*" element={<ErrorPage />} />
    </Routes>
  )
}
