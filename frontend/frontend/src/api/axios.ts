import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8080", // URL вашего Spring Boot бекенда
  withCredentials: false,           // или true, если используешь куки
});