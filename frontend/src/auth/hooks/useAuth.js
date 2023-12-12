import { useContext } from "react";
import AuthContext from "../context/AuthProvider.jsx";

function useAuth() {
  return useContext(AuthContext);
}

export default useAuth;
