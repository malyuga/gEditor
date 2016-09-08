  CREATE TABLE "EDGE" 
   (	"S_ID" NUMBER NOT NULL ENABLE, 
	"F_ID" NUMBER NOT NULL ENABLE, 
	"G_ID" NUMBER NOT NULL ENABLE, 
	"WEIGHT" FLOAT(126), 
	 CONSTRAINT "EDGE_FK1" FOREIGN KEY ("S_ID")
	  REFERENCES "NODE" ("ID") ON DELETE CASCADE ENABLE, 
	 CONSTRAINT "EDGE_FK2" FOREIGN KEY ("F_ID")
	  REFERENCES "NODE" ("ID") ON DELETE CASCADE ENABLE, 
	 CONSTRAINT "EDGE_FK3" FOREIGN KEY ("G_ID")
	  REFERENCES "GRAPH" ("ID") ON DELETE CASCADE ENABLE
   );