import { useEffect, useMemo, useState } from "react";
import type { ClusterDto, ClusterNearbyDto } from "../types/dtos";
import {
  getClusters,
  getNearbyClusters,
  getObservationCount,
} from "../api/observationsApi";
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

  const clustersMeta = useMemo(() => {
    const total = clusters.reduce((acc, c) => acc + (c.size ?? 0), 0);
    return { items: clusters.length, total };
  }, [clusters]);

  const nearbyMeta = useMemo(() => {
    const total = nearbyClusters.reduce((acc, c) => acc + (c.size ?? 0), 0);
    const closest =
      nearbyClusters.length > 0
        ? Math.min(...nearbyClusters.map((c) => c.distanceMeters))
        : null;
    return { items: nearbyClusters.length, total, closest };
  }, [nearbyClusters]);

  return (
    <div className="navPage">
      <div className="navBg" aria-hidden="true" />

      <header className="navHeader">
        <div className="brand">
          <div className="brandDot" />
          <div className="brandText">
            <div className="brandTitle">Traffic Sign Navigator</div>
            <div className="brandSub">Coding Challenge Dashboard</div>
          </div>
        </div>

        <div className="topCenter">
          <div className="countCard">
            <div className="countLabel">Observations</div>
            <div className="countValue">
              {count === null ? "Loading…" : count.toLocaleString()}
            </div>
          </div>
          <div className="pillRow">
            <span className="pill">r = {r}m</span>
            <span className="pill">
              Clusters: {clustersMeta.items} (Σ size {clustersMeta.total})
            </span>
            <span className="pill">
              Nearby: {nearbyMeta.items}
              {nearbyMeta.closest !== null
                ? ` (closest ${nearbyMeta.closest.toFixed(1)}m)`
                : ""}
            </span>
          </div>
        </div>

        <div className="headerRight">
          <div className="statusPill">
            <span className="statusDot" />
            <span className="statusText">LIVE</span>
          </div>
        </div>
      </header>

      {error && (
        <div className="toast" role="alert">
          <div className="toastTitle">Warning</div>
          <div className="toastMsg">{error}</div>
        </div>
      )}

      <main className="navGrid">
        <section className="panel panelLeft">
          <div className="panelHead">
            <div className="panelTitle">
              <span className="panelIcon">◼</span>
              Clusters
            </div>
            <div className="panelHint">Scrollable</div>
          </div>

          <div className="controls">
            <label className="field">
              <span className="fieldLabel">r (m)</span>
              <input
                className="input inputSmall"
                type="number"
                min={1}
                value={r}
                onChange={(e) => setR(Number(e.target.value))}
              />
            </label>

            <button
              className="btn"
              onClick={loadClusters}
              disabled={clustersLoading}
            >
              {clustersLoading ? "Loading..." : "Load clusters"}
            </button>
          </div>

          <div className="tableWrap">
            {clusters.length === 0 ? (
              <div className="empty">
                <div className="emptyTitle">No clusters loaded</div>
                <div className="emptySub">
                  Click <strong>Load clusters</strong> to calculate clusters for r
                  = {r}m.
                </div>
              </div>
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
                      <td>
                        <span className="tag">{c.type}</span>
                      </td>
                      <td className="muted">{c.value ?? "-"}</td>
                      <td className="right strong">{c.size}</td>
                      <td className="right mono">{c.centerLat.toFixed(6)}</td>
                      <td className="right mono">{c.centerLon.toFixed(6)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </section>

        <section className="panel panelRight">
          <div className="panelHead">
            <div className="panelTitle">
              <span className="panelIcon">▣</span>
              Nearby clusters (vehicle view)
            </div>
            <div className="panelHint">Scrollable</div>
          </div>

          <div className="controls controlsWrap">
            <label className="field">
              <span className="fieldLabel">Lat</span>
              <input
                className="input inputMed"
                type="number"
                value={nearbyLat}
                onChange={(e) => setNearbyLat(Number(e.target.value))}
              />
            </label>

            <label className="field">
              <span className="fieldLabel">Lon</span>
              <input
                className="input inputMed"
                type="number"
                value={nearbyLon}
                onChange={(e) => setNearbyLon(Number(e.target.value))}
              />
            </label>

            <label className="field">
              <span className="fieldLabel">Radius (m)</span>
              <input
                className="input inputSmall"
                type="number"
                min={1}
                value={nearbyRadius}
                onChange={(e) => setNearbyRadius(Number(e.target.value))}
              />
            </label>

            <button className="btn" onClick={loadNearby} disabled={nearbyLoading}>
              {nearbyLoading ? "Loading..." : "Load nearby"}
            </button>
          </div>

          <div className="tableWrap">
            {nearbyClusters.length === 0 ? (
              <div className="empty">
                <div className="emptyTitle">No nearby clusters loaded</div>
                <div className="emptySub">
                  Click <strong>Load nearby</strong> to calculate clusters near a
                  vehicle position.
                </div>
              </div>
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
                      <td className="right strong mono">
                        {c.distanceMeters.toFixed(1)}
                      </td>
                      <td>
                        <span className="tag">{c.type}</span>
                      </td>
                      <td className="muted">{c.value ?? "-"}</td>
                      <td className="right strong">{c.size}</td>
                      <td className="right mono">{c.centerLat.toFixed(6)}</td>
                      <td className="right mono">{c.centerLon.toFixed(6)}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </section>
      </main>
    </div>
  );
}
