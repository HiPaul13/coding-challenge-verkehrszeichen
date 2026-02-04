import { useEffect, useState } from "react";
import type { ClusterDto, ClusterNearbyDto } from "../types/dtos";
import { getClusters, getNearbyClusters, getObservationCount } from "../api/observationsApi";
import "./DashboardPage.css";


export default function DashboardPage() {

  const [count, setCount] = useState<number | null>(null);

  const [error, setError] = useState<string | null>(null);

  const [r, setR] = useState<number>(30);
  const [clusters, setClusters] = useState<ClusterDto[]>([]);
  const [clustersLoading, setClustersLoading] = useState(false);
  const [nearbyLat, setNearbyLat] = useState<number>(48.1182);
  const [nearbyLon, setNearbyLon] = useState<number>(11.3738);
  const [nearbyRadius, setNearbyRadius] = useState<number>(200);
  const [nearbyClusters, setNearbyClusters] = useState<ClusterNearbyDto[]>([]);
  const [nearbyLoading, setNearbyLoading] = useState(false);



  useEffect(() => {
    getObservationCount()
      .then((c) => setCount(c))
      .catch(() => setError("Could not reach backend"));
  }, []);

  const loadClusters = async () => {
    try {
      setError(null);
      setClustersLoading(true);
      const data = await getClusters(r);
      setClusters(data);
    } catch {
      setError("Could not load clusters");
    } finally {
      setClustersLoading(false);
    }
  };

    const loadNearby = async () => {
    try {
      setError(null);
      setNearbyLoading(true);
      const data = await getNearbyClusters({
        lat: nearbyLat,
        lon: nearbyLon,
        radius: nearbyRadius,
        r,
      });
      setNearbyClusters(data);
    } catch {
      setError("Could not load nearby clusters");
    } finally {
      setNearbyLoading(false);
    }
  };


  return (
  <div className="page">
    <h1 className="title">Coding Challenge – Traffic Signs</h1>

    {error && <p className="error">{error}</p>}

    <div className="topBar">
      <div className="card">
        <h2 className="cardTitle">Observations</h2>
        {count === null ? (
          <p>Loading…</p>
        ) : (
          <p>
            Count: <strong>{count}</strong>
          </p>
        )}
      </div>
    </div>

    <div className="grid">
      <section className="panel">
        <h2 className="panelTitle">Clusters</h2>

        <div className="controls">
          <label>
            r (m):
            <input
              className="inputSmall"
              type="number"
              min={1}
              value={r}
              onChange={(e) => setR(Number(e.target.value))}
            />
          </label>

          <button onClick={loadClusters} disabled={clustersLoading}>
            {clustersLoading ? "Loading..." : "Load clusters"}
          </button>
        </div>

        <div className="tableWrap">
          {clusters.length === 0 ? (
            <p>No clusters loaded yet.</p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>Type</th>
                  <th>Value</th>
                  <th className="right">Size</th>
                  <th className="right">Lat</th>
                  <th className="right">Lon</th>
                </tr>
              </thead>
              <tbody>
                {clusters.map((c, idx) => (
                  <tr key={idx}>
                    <td>{c.type}</td>
                    <td>{c.value ?? "-"}</td>
                    <td className="right">{c.size}</td>
                    <td className="right">{c.centerLat.toFixed(6)}</td>
                    <td className="right">{c.centerLon.toFixed(6)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </section>

      <section className="panel">
        <h2 className="panelTitle">Nearby clusters (vehicle view)</h2>

        <div className="controls">
          <label>
            Lat:
            <input
              className="inputMed"
              type="number"
              value={nearbyLat}
              onChange={(e) => setNearbyLat(Number(e.target.value))}
            />
          </label>

          <label>
            Lon:
            <input
              className="inputMed"
              type="number"
              value={nearbyLon}
              onChange={(e) => setNearbyLon(Number(e.target.value))}
            />
          </label>

          <label>
            Radius (m):
            <input
              className="inputSmall"
              type="number"
              min={1}
              value={nearbyRadius}
              onChange={(e) => setNearbyRadius(Number(e.target.value))}
            />
          </label>

          <button onClick={loadNearby} disabled={nearbyLoading}>
            {nearbyLoading ? "Loading..." : "Load nearby"}
          </button>
        </div>

        <div className="tableWrap">
          {nearbyClusters.length === 0 ? (
            <p>No nearby clusters loaded yet.</p>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th className="right">Dist (m)</th>
                  <th>Type</th>
                  <th>Value</th>
                  <th className="right">Size</th>
                  <th className="right">Lat</th>
                  <th className="right">Lon</th>
                </tr>
              </thead>
              <tbody>
                {nearbyClusters.map((c, idx) => (
                  <tr key={idx}>
                    <td className="right">{c.distanceMeters.toFixed(1)}</td>
                    <td>{c.type}</td>
                    <td>{c.value ?? "-"}</td>
                    <td className="right">{c.size}</td>
                    <td className="right">{c.centerLat.toFixed(6)}</td>
                    <td className="right">{c.centerLon.toFixed(6)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </section>
    </div>
  </div>
);

}
