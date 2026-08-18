import React from "react";
import { Button, Typography } from "@mui/material";
import { Link } from "react-router-dom";

const Dashboard = () => {
  return (
    <div>

      <Typography
        variant="h3"
        align="center"
        sx={{ margin: "5%" }}
      >
        Employer Dashboard
      </Typography>

      <div style={{ textAlign: "center" }}>

        <Button
          variant="outlined"
          sx={{ margin: "2%" }}
        >
          <Link to="/">
            Home
          </Link>
        </Button>


        <Button
          variant="contained"
          sx={{ margin: "2%" }}
        >
          <Link
            to="/employer/create"
            style={{
              color: "white",
              textDecoration: "none",
            }}
          >
            Create Job
          </Link>
        </Button>

      </div>

    </div>
  );
};

export default Dashboard;
