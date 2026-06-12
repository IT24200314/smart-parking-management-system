import { useState } from 'react';
import { Navigate } from 'react-router-dom';
import { LogIn, UserPlus } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export default function Login() {
  const { user, login, register } = useAuth();
  const [mode, setMode] = useState('login');
  const [form, setForm] = useState({ fullName: '', email: 'admin@smartparkpro.com', password: 'password', role: 'ADMIN' });
  const [error, setError] = useState('');

  if (user) return <Navigate to="/" replace />;

  async function submit(event) {
    event.preventDefault();
    setError('');
    try {
      if (mode === 'login') await login(form.email, form.password);
      else await register(form);
    } catch (err) {
      setError(err.response?.data?.message || 'Authentication failed');
    }
  }

  return (
    <main className="grid min-h-screen place-items-center bg-[radial-gradient(circle_at_top_left,#dff4ef,transparent_32%),#f8fafc] px-4">
      <form onSubmit={submit} className="w-full max-w-md rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
        <h1 className="text-2xl font-semibold">SmartParkPro</h1>
        <p className="mt-1 text-sm text-slate-500">Enterprise parking operations console</p>
        <div className="mt-6 grid grid-cols-2 rounded-md bg-slate-100 p-1">
          <button type="button" onClick={() => setMode('login')} className={`rounded px-3 py-2 text-sm ${mode === 'login' ? 'bg-white shadow-sm' : ''}`}>Login</button>
          <button type="button" onClick={() => setMode('register')} className={`rounded px-3 py-2 text-sm ${mode === 'register' ? 'bg-white shadow-sm' : ''}`}>Register</button>
        </div>
        {mode === 'register' && <Field label="Full name" value={form.fullName} onChange={(fullName) => setForm({ ...form, fullName })} />}
        <Field label="Email" type="email" value={form.email} onChange={(email) => setForm({ ...form, email })} />
        <Field label="Password" type="password" value={form.password} onChange={(password) => setForm({ ...form, password })} />
        {mode === 'register' && (
          <label className="mt-4 block text-sm">
            Role
            <select className="mt-1 h-10 w-full rounded-md border border-slate-300 px-3" value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}>
              <option>ADMIN</option>
              <option>STAFF</option>
              <option>CUSTOMER</option>
            </select>
          </label>
        )}
        {error && <p className="mt-4 rounded-md bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}
        <button className="mt-5 inline-flex h-11 w-full items-center justify-center gap-2 rounded-md bg-pine px-4 font-medium text-white hover:bg-teal-800">
          {mode === 'login' ? <LogIn size={18} /> : <UserPlus size={18} />}
          {mode === 'login' ? 'Login' : 'Create account'}
        </button>
      </form>
    </main>
  );
}

function Field({ label, value, onChange, type = 'text' }) {
  return (
    <label className="mt-4 block text-sm">
      {label}
      <input className="mt-1 h-10 w-full rounded-md border border-slate-300 px-3" type={type} value={value} onChange={(e) => onChange(e.target.value)} required />
    </label>
  );
}
