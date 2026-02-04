import type { ClusterDto, ClusterNearbyDto } from "../types/dtos";

export async function getObservationCount(): Promise<number> {
  const res = await fetch("/api/observations/count");
  if (!res.ok) throw new Error("Failed to load count");
  return res.json();
}

export async function getClusters(r: number): Promise<ClusterDto[]> {
  const res = await fetch(`/api/observations/clusters?r=${r}`);
  if (!res.ok) throw new Error("Failed to load clusters");
  return res.json();
}

export async function getNearbyClusters(params: {
  lat: number;
  lon: number;
  radius: number;
  r: number;
}): Promise<ClusterNearbyDto[]> {
  const { lat, lon, radius, r } = params;
  const res = await fetch(
    `/api/observations/clusters/nearby?lat=${lat}&lon=${lon}&radius=${radius}&r=${r}`
  );
  if (!res.ok) throw new Error("Failed to load nearby clusters");
  return res.json();
}
