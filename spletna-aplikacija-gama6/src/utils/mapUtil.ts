/*eslint-disable*/
function isPointInsidePolygon(
  point: { lat : number,lng: number },
  polygon?: Array<{ lat: number; lng: number }>
): boolean {
  if (!polygon || polygon.length < 3) {
    return false;
  }

  let isInside = false;

  for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
    const xi = polygon[i]?.lat || 0;
    const yi = polygon[i]?.lng || 0;
    const xj = polygon[j]?.lat || 0;
    const yj = polygon[j]?.lng || 0;
    const intersect =
      yi > point.lng !== yj > point.lng &&
      point.lat < ((xj - xi) * (point.lng - yi)) / (yj - yi) + xi;
    if (intersect) {
      isInside = !isInside;
    }
  }

  return isInside;
}

export { isPointInsidePolygon };
