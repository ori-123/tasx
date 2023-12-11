import {useCallback} from "react";
import useAuth from "../auth/hooks/useAuth.js";
import useLogout from "../auth/hooks/useLogout.js";
import useRefresh from "../auth/hooks/useRefresh.js";

function useAuthFetch() {
  const {auth} = useAuth();
  const logout = useLogout();
  const refresh = useRefresh();
  // eslint-disable-next-line no-undef
  const baseUrl = import.meta.env.VITE_API_BASE_URL;

  async function handleRefresh(url, requestConfig, authErrorStatus) {
    const refreshResponse = await refresh();
    const refreshedAccessToken = refreshResponse?.data?.accessToken;
    let httpResponse;
    if (refreshedAccessToken) {
      requestConfig.headers.Authorization = `Bearer ${refreshedAccessToken}`;
      httpResponse = await fetch(url, requestConfig);
      if (httpResponse.status === authErrorStatus) {
        return await logout();
      }
    }
    return httpResponse;
  }

  const authFetch = useCallback(async (method, path, body) => {
      const authErrorStatus = 401 || 403;
      const url = `${baseUrl}/${path}`;
      const requestConfig = {
        method: `${method}`, headers: {
          "Content-Type": "application/json"
        }, credentials: "include"
      };
      if (auth?.accessToken) {
        requestConfig.headers.Authorization = `Bearer ${auth.accessToken}`;
      }
      if (body) {
        requestConfig.body = JSON.stringify(body);
      }
      let httpResponse = await fetch(url, requestConfig);
      if ((path !== "auth/login" && httpResponse.status === authErrorStatus)) {
        httpResponse = await handleRefresh(url, requestConfig, authErrorStatus);
      }
      const responseObject = await httpResponse.json();
      return {"httpResponse": httpResponse, "responseObject": responseObject};
    }, // eslint-disable-next-line react-hooks/exhaustive-deps
    [auth.accessToken]);

  return authFetch;
}

export default useAuthFetch;
