import { useEffect, useState } from 'react';
import { api } from '../services/api';

export default function AnalyticsPage() {
  const [data, setData] = useState(null);
  const [advanced, setAdvanced] = useState(null);
  const [vehicleType, setVehicleType] = useState('CAR');

  useEffect(() => {
    api.get(`/analytics?vehicleType=${vehicleType}`).then((response) => setData(response.data));
    api.get('/ai/predict-demand').then((response) => setAdvanced(response.data));
  }, [vehicleType]);

  return (
    <div>
      <div className="flex items-center justify-between">
        <h2 className="text-2xl font-semibold">AI Analytics</h2>
        <select value={vehicleType} onChange={(e) => setVehicleType(e.target.value)} className="h-10 rounded-md border border-slate-300 px-3 text-sm">
          <option>CAR</option>
          <option>EV</option>
          <option>VAN</option>
          <option>MOTORCYCLE</option>
          <option>TRUCK</option>
        </select>
      </div>
      <div className="mt-5 grid gap-5 lg:grid-cols-2">
        <Panel title="Occupancy & Revenue Prediction">
          <div className="grid gap-3 text-sm sm:grid-cols-2">
            <Metric label="Occupancy" value={`${advanced?.occupancyRate ?? 0}%`} />
            <Metric label="Peak Window" value={advanced?.peakHourPrediction ?? 'Loading'} />
            <Metric label="Available" value={advanced?.availableSlots ?? 0} />
            <Metric label="Predicted Revenue" value={`$${advanced?.predictedDailyRevenue ?? 0}`} />
          </div>
        </Panel>
        <Panel title="Demand Prediction">
          {data?.demandPredictions?.map((item) => (
            <div key={item.label} className="flex items-center justify-between border-b border-slate-100 py-3 text-sm">
              <span>{item.label}</span>
              <strong>{item.predictedVehicles} vehicles</strong>
              <span className="text-slate-500">{item.confidence}</span>
            </div>
          ))}
        </Panel>
        <Panel title="Slot Recommendations">
          {data?.recommendations?.map((slot) => (
            <div key={slot.slotId} className="border-b border-slate-100 py-3 text-sm">
              <div className="font-medium">{slot.slotCode} · {slot.parkingLotName}</div>
              <div className="text-slate-500">{slot.reason}</div>
            </div>
          ))}
        </Panel>
      </div>
    </div>
  );
}

function Panel({ title, children }) {
  return (
    <section className="rounded-lg border border-slate-200 bg-white p-5">
      <h3 className="font-semibold">{title}</h3>
      <div className="mt-3">{children}</div>
    </section>
  );
}

function Metric({ label, value }) {
  return (
    <div className="rounded-md border border-slate-200 p-3">
      <div className="text-slate-500">{label}</div>
      <div className="mt-1 font-semibold">{value}</div>
    </div>
  );
}
