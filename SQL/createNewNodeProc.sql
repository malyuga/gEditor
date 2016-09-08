CREATE OR REPLACE PROCEDURE NEW_NODE 
(
  OUT_ID IN OUT NUMBER 
, IN_G_ID IN NUMBER 
, IN_NUMB IN NUMBER 
, IN_NAME IN VARCHAR2 
, IN_INFO IN VARCHAR2 
, IN_X IN FLOAT 
, IN_Y IN FLOAT 
) AS 
BEGIN
  IF OUT_ID = 0 THEN 
    OUT_ID := SEQ_NODE_ID.nextval();
  END IF;
  INSERT INTO NODE
  VALUES(OUT_ID, IN_G_ID, IN_NUMB, IN_NAME, IN_INFO, IN_X, IN_Y);
END NEW_NODE;