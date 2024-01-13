from fastapi import FastAPI, Body, Request, Response
from pymongo import MongoClient
DATABASE_URL="mongodb+srv://admin-gal:FaBbYMOvJ0xlAuaa@gama6.e3onpri.mongodb.net/?retryWrites=true&w=majority"

from pydantic import BaseModel, Field
from fastapi.encoders import jsonable_encoder

class Location(BaseModel):
    name : str


client = MongoClient(DATABASE_URL)
db = client.gama6DB


app = FastAPI()


@app.get("/")
async def root():
    return {"message": "Hello World"}

@app.post("/location")
async def location(location):
    print(location)
    return location
   # new_location = db.location.insert_one(location)
    #created_location = db.location.find_one({"_id": new_location.inserted_id})
    #return {"message": "Location created successfully", "data": created_location}

    

@app.get("/locations")
async def locations():
    locations = list(db.location.find())
    return [Location(**loc) for loc in locations]

    

    
    