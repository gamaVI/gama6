from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
import uvicorn
from vehicle_count import *

app = FastAPI()


@app.post("/process-image/")
async def process_image(file: UploadFile = File(...)):
    with open("temp_image.jpg", "wb") as buffer:
        buffer.write(file.file.read())

    result = from_static_image("temp_image.jpg")
    print(result)
    
    return JSONResponse(content={"count": result})

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=7000)
