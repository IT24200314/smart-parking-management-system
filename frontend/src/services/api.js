import axios from 'axios';

export const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api'
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('spp_token');
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export const endpoints = {
  parkingLots: '/parking-lots',
  parkingSlots: '/parking-slots',
  vehicles: '/vehicles',
  bookings: '/bookings',
  payments: '/payments',
  users: '/users'
};
