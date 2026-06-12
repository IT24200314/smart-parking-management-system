import { useEffect, useState } from 'react';
import { Car, CircleDollarSign, ParkingCircle, Rows3 } from 'lucide-react';
import { api } from '../services/api';

export default function Dashboard() {
  const [dashboard, setDashboard] = useState(null);
  const [activities, setActivities] = useState([]);

  useEffect(() => {
    Promise.all([api.get('/dashboard'), api.get('/recent-activities')]).then(([d, a]) => {
      setDashboard(d.data);
      setActivities(a.data);
    });
  }, []);

  const cards = [
    ['Total Vehicles', dashboard?.totalVehicles ?? 0, Car],
    ['Available Slots', dashboard?.availableSlots ?? 0, Rows3],
    ['Occupied Slots', dashboard?.occupiedSlots ?? 0, ParkingCircle],
    ['Revenue', `$${dashboard?.revenue ?? 0}`, CircleDollarSign]
  ];

  return (
    <div>
      <h2 className="text-2xl font-semibold">Operations Dashboard</h2>
      <div className="mt-5 grid gap-4 sm:grid-cols-2 xl:grid-cols-4">
        {cards.map(([label, value, Icon]) => (
          <div key={label} className="rounded-lg border border-slate-200 bg-white p-5">
            <div className="flex items-center justify-between">
              <p className="text-sm text-slate-500">{label}</p>
              <Icon className="text-pine" size={20} />
            </div>
            <p className="mt-3 text-3xl font-semibold">{value}</p>
          </div>
        ))}
      </div>
      <section className="mt-6 rounded-lg border border-slate-200 bg-white">
        <div className="border-b border-slate-200 px-5 py-4">
          <h3 className="font-semibold">Recent Parking Activity Stack</h3>
        </div>
        <div className="divide-y divide-slate-100">
          {activities.map((activity) => (
            <div key={activity.id} className="grid gap-1 px-5 py-3 text-sm md:grid-cols-[180px_1fr_120px]">
              <span className="font-medium text-pine">{activity.activityType}</span>
              <span>{activity.description}</span>
              <span className="text-slate-500">{activity.licensePlate || activity.slotCode || ''}</span>
            </div>
          ))}
          {!activities.length && <p className="px-5 py-8 text-sm text-slate-500">No activity recorded yet.</p>}
        </div>
      </section>
    </div>
  );
}
