import React, { useState } from "react";
import axios from "axios";

import {
  Box,
  Button,
  TextField,
  Typography,
} from "@mui/material";

import { Link, useNavigate } from "react-router-dom";


const Create = () => {

  const navigate = useNavigate();

  const [profile, setProfile] = useState("");
  const [desc, setDesc] = useState("");
  const [exp, setExp] = useState("");
  const [tech, setTech] = useState("");


  const handleSubmit = async (e) => {

    e.preventDefault();

    try {

      const job = {
        profile: profile,
        desc: desc,
        exp: Number(exp),

        // Convert:
        // "java, spring, mongodb"
        //
        // into:
        // ["java", "spring", "mongodb"]

        tech: tech
          .split(",")
          .map((item) => item.trim())
          .filter((item) => item.length > 0),
      };


      console.log("Sending job:", job);


      await axios.post(
        "http://localhost:8080/api/job",
        job
      );


      alert("Job created successfully!");

      navigate("/employee/feed");


    } catch (error) {

      console.error(
        "Error creating job:",
        error
      );

      alert("Failed to create job");

    }

  };


  return (
    <Box
      sx={{
        width: "50%",
        margin: "5% auto",
      }}
    >

      <Typography
        variant="h3"
        align="center"
        sx={{ marginBottom: "5%" }}
      >
        Create Job
      </Typography>


      <form onSubmit={handleSubmit}>

        <TextField
          fullWidth
          label="Job Profile"
          value={profile}
          onChange={(e) =>
            setProfile(e.target.value)
          }
          sx={{ marginBottom: "3%" }}
        />


        <TextField
          fullWidth
          label="Description"
          multiline
          rows={4}
          value={desc}
          onChange={(e) =>
            setDesc(e.target.value)
          }
          sx={{ marginBottom: "3%" }}
        />


        <TextField
          fullWidth
          label="Years of Experience"
          type="number"
          value={exp}
          onChange={(e) =>
            setExp(e.target.value)
          }
          sx={{ marginBottom: "3%" }}
        />


        <TextField
          fullWidth
          label="Skills"
          placeholder="java, spring, mongodb"
          value={tech}
          onChange={(e) =>
            setTech(e.target.value)
          }
          sx={{ marginBottom: "3%" }}
        />


        <Button
          type="submit"
          variant="contained"
          sx={{ marginRight: "2%" }}
        >
          Create Job
        </Button>


        <Button variant="outlined">

          <Link to="/">
            Cancel
          </Link>

        </Button>

      </form>

    </Box>
  );
};

export default Create;
