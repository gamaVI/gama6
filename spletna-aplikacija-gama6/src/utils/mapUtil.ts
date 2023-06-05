function isPointInsidePolygon(
  point: { lat: number; lng: number },
  polygon?: Array<{ lat: number; lng: number }>
): boolean {
  if (!polygon || polygon.length < 3) {
    return false;
  }

  // Bounding box check
  const minLng = Math.min(...polygon.map(p => p.lng));
  const maxLng = Math.max(...polygon.map(p => p.lng));
  const minLat = Math.min(...polygon.map(p => p.lat));
  const maxLat = Math.max(...polygon.map(p => p.lat));

  if (point.lng < minLng || point.lng > maxLng || point.lat < minLat || point.lat > maxLat) {
    return false;
  }

  let isInside = false;

  for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
    const xi = polygon[i]?.lng || 0;
    const yi = polygon[i]?.lat || 0;
    const xj = polygon[j]?.lng || 0;
    const yj = polygon[j]?.lat || 0;
    const epsilon = 0.0000001;  // A small number

    const intersect =
      yi > point.lat !== yj > point.lat &&
      point.lng < ((xj - xi) * (point.lat - yi)) / (yj - yi) + xi + epsilon;
    
    if (intersect) {
      isInside = !isInside;
    }
  }

  return isInside;
}


export { isPointInsidePolygon };