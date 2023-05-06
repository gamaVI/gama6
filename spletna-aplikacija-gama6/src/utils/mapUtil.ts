function isPointInsidePolygon(
  point: { x: number; y: number },
  polygon?: Array<{ x: number; y: number }>
): boolean {
  if (!polygon || polygon.length < 3) {
    return false;
  }

  let isInside = false;

  for (let i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
    const xi = polygon[i]?.x || 0;
    const yi = polygon[i]?.y || 0;
    const xj = polygon[j]?.x || 0;
    const yj = polygon[j]?.y || 0;
    const intersect =
      yi > point.y !== yj > point.y &&
      point.x < ((xj - xi) * (point.y - yi)) / (yj - yi) + xi;
    if (intersect) {
      isInside = !isInside;
    }
  }

  return isInside;
}

export { isPointInsidePolygon };
