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
    const xi = polygon[i]?.lng || 0;
    const yi = polygon[i]?.lat || 0;
    const xj = polygon[j]?.lng || 0;
    const yj = polygon[j]?.lat || 0;
    const intersect =
      yi > point.lat !== yj > point.lat &&
      point.lng < ((xj - xi) * (point.lat - yi)) / (yj - yi) + xi;
    if (intersect) {
      isInside = !isInside;
    }
  }

  return isInside;
}



export { isPointInsidePolygon };
