import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import AppShell from './components/AppShell';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import ResourcePage from './pages/ResourcePage';
import QueuePage from './pages/QueuePage';
import AnalyticsPage from './pages/AnalyticsPage';
import './index.css';

function PrivateRoute({ children }) {
  const { user } = useAuth();
  return user ? children : <Navigate to="/login" replace />;
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/" element={<PrivateRoute><AppShell /></PrivateRoute>}>
            <Route index element={<Dashboard />} />
            <Route path="lots" element={<ResourcePage resource="parkingLots" title="Parking Lots" />} />
            <Route path="slots" element={<ResourcePage resource="parkingSlots" title="Parking Slots" />} />
            <Route path="vehicles" element={<ResourcePage resource="vehicles" title="Vehicles" />} />
            <Route path="bookings" element={<ResourcePage resource="bookings" title="Bookings" />} />
            <Route path="payments" element={<ResourcePage resource="payments" title="Payments" />} />
            <Route path="users" element={<ResourcePage resource="users" title="Users" />} />
            <Route path="queue" element={<QueuePage />} />
            <Route path="analytics" element={<AnalyticsPage />} />
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  </React.StrictMode>
);
