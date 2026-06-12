import { useEffect, useMemo, useState } from 'react';
import { RefreshCw, Trash2 } from 'lucide-react';
import { api, endpoints } from '../services/api';

export default function ResourcePage({ resource, title }) {
  const [rows, setRows] = useState([]);
  const endpoint = endpoints[resource];

  const columns = useMemo(() => rows[0] ? Object.keys(rows[0]).filter((key) => !String(key).toLowerCase().includes('id')).slice(0, 7) : [], [rows]);

  async function load() {
    const { data } = await api.get(endpoint);
    setRows(data);
  }

  async function remove(id) {
    await api.delete(`${endpoint}/${id}`);
    await load();
  }

  useEffect(() => { load(); }, [endpoint]);

  return (
    <div>
      <div className="flex items-center justify-between gap-3">
        <h2 className="text-2xl font-semibold">{title}</h2>
        <button onClick={load} className="inline-flex h-10 items-center gap-2 rounded-md border border-slate-300 px-3 text-sm hover:bg-slate-50">
          <RefreshCw size={16} />
          Refresh
        </button>
      </div>
      <div className="mt-5 overflow-hidden rounded-lg border border-slate-200 bg-white">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-slate-200 text-sm">
            <thead className="bg-slate-50">
              <tr>
                {columns.map((column) => <th key={column} className="px-4 py-3 text-left font-medium capitalize text-slate-600">{column}</th>)}
                <th className="px-4 py-3 text-right font-medium text-slate-600">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100">
              {rows.map((row) => (
                <tr key={row.id}>
                  {columns.map((column) => <td key={column} className="max-w-[220px] truncate px-4 py-3">{String(row[column] ?? '')}</td>)}
                  <td className="px-4 py-3 text-right">
                    <button title="Delete" onClick={() => remove(row.id)} className="inline-grid h-8 w-8 place-items-center rounded-md text-red-600 hover:bg-red-50">
                      <Trash2 size={16} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        {!rows.length && <p className="px-5 py-8 text-sm text-slate-500">No records found.</p>}
      </div>
    </div>
  );
}
