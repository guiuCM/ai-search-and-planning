import random

MAX_HAB = 10
MAX_R = 6
MAX_DIES_R = 7
MAX_CAP = 10

ORIENTACIONS = ['N', 'S', 'E', 'O']

NUM_CASOS = 5

def random_subrange(max_value=30):
    start = random.randint(1, 30)
    length = random.randint(1, MAX_DIES_R)
    end = min(start + length - 1, 30)
    return start, end


def writeMes(f):
    f.write("   ")
    for d in range(1,31):
        f.write('d'+str(d) + ' ')
    f.write("- dia\n")

def writeOrientacio(f):
    f.write("   ")
    for o in ORIENTACIONS:
        f.write(o + ' ')
    f.write("- orientacio\n")

def writeHab(f, n):
    f.write("   ")
    for i in range(n+1):
        f.write('h'+str(i) + ' ')
    f.write("- habitacio\n")

def writeRes(f, n):
    f.write("   ")
    for i in range(n+1):
        f.write('r'+str(i) + ' ')
    f.write("- reserva\n")

def writeInitRres(f, res):
    pers = random.randint(1, MAX_CAP)
    ini, fi = random_subrange()
    o = random.choice(ORIENTACIONS)
    f.write("   ")
    r = 'r'+str(res)
    f.write("(vol_orientacio " + r + " " + o + ")\n")
    f.write("   ")
    f.write("(= (persones_reserva " + r + " " + ") " + str(pers) + " )\n")

    for i in range(ini, fi+1):
        f.write("   ")
        d = "d"+str(i)
        f.write("(dia_reserva " + r + " " + d + ")\n")
    
    
def writeInitHab(f, hab):
    pers = random.randint(1, MAX_CAP)
    o = random.choice(ORIENTACIONS)
    f.write("   ")
    h = 'h'+str(hab)
    f.write("(te_orientacio " + h + " " + o + ")\n")
    f.write("   ")
    f.write("(= (capacitat_habitacio " + h + " ) " + str(pers) + " )\n")
    f.write("   ")
    f.write("(nova_habitacio " + h + ")\n")

for i in range(NUM_CASOS):
    filename = "problema_" + str(i) + ".pddl"
    f = open(filename, "x")

    prob_name = "(problem hotel_" + str(i) + "-problem)"
    f.write("(define" + prob_name + "\n    (:domain hotel)\n")

    #nombre objectes del problema i
    num_h = random.randint(1, MAX_HAB)
    #num_dies = random.randint(1, MAX_DIES_R) #per cada reserva
    num_r = random.randint(1, MAX_R)
    
    #objects
    f.write("(:objects\n")
    writeHab(f, num_h)
    writeRes(f, num_r)
    writeMes(f)
    writeOrientacio(f)
    f.write(")\n")

    #init
    f.write("(:init\n")

    for h in range(num_h+1):
        writeInitHab(f,h)
        f.write("\n")

    for r in range(num_r+1):
        writeInitRres(f,r)
        f.write("\n")

    f.write("(= (total-cost) 0)\n")

    f.write(")\n")

    goal = """\
(:goal 
  (forall (?r - reserva) 
     (or (reserva_servida ?r) (reserva_no_servida ?r))
  )
)
(:metric minimize (total-cost))
"""
    f.write(goal)

    # fi define
    f.write(')')
