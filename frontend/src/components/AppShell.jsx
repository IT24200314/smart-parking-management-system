import { NavLink, Outlet } from 'react-router-dom';
import { Activity, BarChart3, CalendarClock, Car, CreditCard, LayoutDashboard, LogOut, ParkingCircle, Rows3, Users } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

const nav = [
  ['/', 'Dashboard', LayoutDashboard],
  ['/lots', 'Lots', ParkingCircle],
  ['/slots', 'Slots', Rows3],
  ['/vehicles', 'Vehicles', Car],
  ['/bookings', 'Bookings', CalendarClock],
  ['/payments', 'Payments', CreditCard],
  ['/queue', 'Queue', Activity],
  ['/analytics', 'Analytics', BarChart3],
  ['/users', 'Users', Users]
];

export default function AppShell() {
  const { user, logout } = useAuth();
  return (
    <div className="min-h-screen">
      <aside className="fixed inset-y-0 left-0 hidden w-64 border-r border-slate-200 bg-white md:block">
        <div className="border-b border-slate-200 px-5 py-5">
          <h1 className="text-xl font-semibold tracking-normal">SmartParkPro</h1>
          <p className="mt-1 text-sm text-slate-500">{user?.role} workspace</p>
        </div>
        <nav className="space-y-1 px-3 py-4">
          {nav.map(([to, label, Icon]) => (
            <NavLink key={to} to={to} end={to === '/'} className={({ isActive }) => `flex items-center gap-3 rounded-md px-3 py-2 text-sm ${isActive ? 'bg-mist text-pine' : 'text-slate-600 hover:bg-slate-50'}`}>
              <Icon size={18} />
              <span>{label}</span>
            </NavLink>
          ))}
        </nav>
      </aside>
      <main className="md:pl-64">
        <header className="sticky top-0 z-10 flex items-center justify-between border-b border-slate-200 bg-white/95 px-4 py-3 backdrop-blur md:px-8">
          <div>
            <p className="text-sm text-slate-500">Signed in as</p>
            <p className="font-medium">{user?.fullName || user?.email}</p>
          </div>
          <button onClick={logout} className="inline-flex h-10 items-center gap-2 rounded-md border border-slate-300 px-3 text-sm hover:bg-slate-50">
            <LogOut size={17} />
            Logout
          </button>
        </header>
        <section className="px-4 py-6 md:px-8">
          <Outlet />
        </section>
      </main>
    </div>
  );
}
