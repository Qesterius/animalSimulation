package agh.ics.oop;

public enum MoveDirection {
        FORWARD,
        ROT45,
        ROT90,
        ROT135,
        BACKWARD,
        ROT225,
        ROT270,
        ROT315;

        public Integer toInt()
        {
                Integer out = null;
                switch(this) {
                        case FORWARD -> out = 0;
                        case ROT45 -> out = 1;
                        case ROT90 -> out = 2;
                        case ROT135 -> out = 3;
                        case BACKWARD -> out = 4;
                        case ROT225 -> out = 5;
                        case ROT270 -> out = 6;
                        case ROT315 -> out = 7;
                }
                return out;
        }
        public MoveDirection createDir(int a)
        {
                return switch (a){
                        case 0->FORWARD;
                        case 1->ROT45;
                        case 2->ROT90;
                        case 3->ROT135;
                        case 4->BACKWARD ;
                        case 5->ROT225;
                        case 6->ROT270;
                        case 7->ROT315;

                        default -> throw new IllegalStateException("Unexpected value: " + a);
                };
        }



}
