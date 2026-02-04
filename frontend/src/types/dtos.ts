export type ClusterDto = {
  centerLat: number;
  centerLon: number;
  type: string;
  value: string | null;
  size: number;
};

export type ClusterNearbyDto = ClusterDto & {
  distanceMeters: number;
};
