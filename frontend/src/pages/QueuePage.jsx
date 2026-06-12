import { useState } from 'react';
import { Plus, RefreshCw } from 'lucide-react';
import { api } from '../services/api';

export default function QueuePage() {
  const [parkingLotId, setParkingLotId] = useState('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa');
  const [vehicleId, setVehicleId] = useState('55555555-5555-5555-5555-555555555555');
  const [rows, setRows] = useState([]);

  async function load() {
    const { data } = await api.get(`/waiting-queue/${parkingLotId}`);
    setRows(data);
  }

  async function enqueue() {
    await api.post('/waiting-queue', { vehicleId, parkingLotId, requestedType: 'EV' });
    await load();
  }

  return (
    <div>
      <h2 className="text-2xl font-semibold">Waiting Vehicle Queue</h2>
      <div className="mt-5 grid gap-3 rounded-lg border border-slate-200 bg-white p-4 md:grid-cols-[1fr_1fr_auto_auto]">
        <input className="h-10 rounded-md border border-slate-300 px-3 text-sm" value={parkingLotId} onChange={(e) => setParkingLotId(e.target.value)} />
        <input className="h-10 rounded-md border border-slate-300 px-3 text-sm" value={vehicleId} onChange={(e) => setVehicleId(e.target.value)} />
        <button onClick={enqueue} className="inline-flex h-10 items-center justify-center gap-2 rounded-md bg-pine px-3 text-sm text-white"><Plus size={16} /> Enqueue</button>
        <button onClick={load} className="inline-flex h-10 items-center justify-center gap-2 rounded-md border border-slate-300 px-3 text-sm"><RefreshCw size={16} /> Load</button>
      </div>
      <div className="mt-5 rounded-lg border border-slate-200 bg-white">
        {rows.map((row) => (
          <div key={row.id} className="grid gap-2 border-b border-slate-100 px-5 py-3 text-sm md:grid-cols-[90px_1fr_1fr]">
            <span className="font-semibold text-pine">#{row.queuePosition}</span>
            <span>{row.licensePlate}</span>
            <span className="text-slate-500">{row.parkingLotName}</span>
          </div>
        ))}
        {!rows.length && <p className="px-5 py-8 text-sm text-slate-500">Load a lot queue to view waiting vehicles.</p>}
      </div>
    </div>
  );
}
