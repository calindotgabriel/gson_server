CREATE table pharmacy (
    id          INTEGER NOT NULL
                PRIMARY KEY GENERATED ALWAYS AS IDENTITY
                (START WITH 1, INCREMENT BY 1)
)
