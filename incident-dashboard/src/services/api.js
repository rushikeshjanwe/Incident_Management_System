import axios from 'axios';

const API_URL = 'http://localhost:8081/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to every request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Auth APIs
export const login = (username, password) => {
  return api.post('/auth/login', { username, password });
};

export const register = (userData) => {
  return api.post('/auth/register', userData);
};

// Incident APIs
export const getIncidents = () => {
  return api.get('/incidents');
};

export const getIncidentById = (id) => {
  return api.get(`/incidents/${id}`);
};

export const createIncident = (incident) => {
  return api.post('/incidents', incident);
};

export const acknowledgeIncident = (id) => {
  return api.patch(`/incidents/${id}/acknowledge`);
};

export const resolveIncident = (id, resolution) => {
  return api.patch(`/incidents/${id}/resolve?resolution=${encodeURIComponent(resolution)}`);
};

export const closeIncident = (id) => {
  return api.patch(`/incidents/${id}/close`);
};

export const escalateIncident = (id) => {
  return api.patch(`/incidents/${id}/escalate`);
};

export default api;