import { useEffect, useState } from "react";
import { Navigate, useLocation } from "react-router";
import { useUserContext } from "@hooks";
import { IUserInfo } from "@interfaces/index";
import { USER_ACTION } from "@contexts/UserContext";
import { AppRequest } from "@requests/index";
import { toast } from "react-toastify";

export const ProtectedRoute = ({ children }: { children: React.ReactNode }) => {
  const { user, dispatchUser } = useUserContext();
  const [loading, setLoading] = useState(true);
  const location = useLocation();

  useEffect(() => {
    if (!user) {
      fetchUser();
    } else {
      setLoading(false);
    }
  }, [user]);

  const fetchUser = async () => {
    try {
      const resp = await AppRequest.getInstance().get("/api/self-user-info");
      const userInfo: IUserInfo = resp.data?.data
      dispatchUser({
        type: USER_ACTION.ADD,
        payload: userInfo
      })
    } catch (err) {
      toast.error(err)
      localStorage.removeItem("jwt");
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <div>Loading...</div>;

  if (!user) return <Navigate to="/auth/login" state={{ from: location }} replace />;

  return <>{children}</>;
};
