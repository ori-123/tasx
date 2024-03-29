import React from "react";
import BackButton from "../../components/BackButton.jsx";

function NotFound() {
  return (
    <div className="NotFound">
      <h2>The page you are looking for does not exist.</h2>
      <BackButton />
    </div>
  );
}

export default NotFound;
