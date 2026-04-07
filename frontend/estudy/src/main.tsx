import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { App } from '@/app/App'
import './index.css'
import 'remixicon/fonts/remixicon.css'
import { Toaster } from '@/components/base/sonner'
import { ThemeProvider } from 'next-themes'
import { GoogleOAuthProvider } from '@react-oauth/google'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <>
      <ThemeProvider attribute="class" enableSystem>
        <Toaster />
        <GoogleOAuthProvider clientId={import.meta.env.ESTUDY_GOOGLE_CLIENT_ID}>
          <App />
        </GoogleOAuthProvider>
      </ThemeProvider>
    </>
  </StrictMode>,
)
