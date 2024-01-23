from fastapi import FastAPI, File, UploadFile
from fastapi.responses import JSONResponse
import uvicorn
from vehicle_count import *



if __name__ == "__main__":
    result = from_static_image("peugeot.png")
    print(f"""result: {result}""")

