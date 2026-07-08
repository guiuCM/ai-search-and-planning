import numpy as np
import matplotlib.pyplot as plt

''' primera vegada'''
# resumBenefici:
benefici = [44104.0,44104.0,44104.0,44104.0,44104.0,44104.0,44104.0,44104.0,44104.0,44104.0]
treure_benefici = [44104.0,44104.0,44104.0,44104.0,44104.0,44104.0,44104.0,44104.0,44104.0,44104.0]
afegir_benefici = [62524.0,62524.0,62524.0,62524.0,62524.0,62524.0,62524.0,62524.0,62524.0,62524.0]
treure_afegir_benefici = [62524.0,62524.0,62524.0,62524.0,62524.0,62524.0,62524.0,62524.0,62524.0,62524.0]
moure_benefici = [51764.0,51764.0,51764.0,51764.0,51764.0,51764.0,51764.0,51764.0,51764.0,51764.0]
treure_moure_benefici = [51764.0,51764.0,51764.0,51764.0,51764.0,51764.0,51764.0,51764.0,51764.0,51764.0]
afegir_moure_benefici = [80044.0,80044.0,80044.0,80044.0,80044.0,80044.0,80044.0,80044.0,80044.0,80044.0]
treure_afegir_moure_benefici = [80044.0,80044.0,80044.0,80044.0,80044.0,80044.0,80044.0,80044.0,80044.0,80044.0]
swap_benefici = [51436.0,51436.0,51436.0,51436.0,51436.0,51436.0,51436.0,51436.0,51436.0,51436.0]
treure_swap_benefici = [51436.0,51436.0,51436.0,51436.0,51436.0,51436.0,51436.0,51436.0,51436.0,51436.0]
afegir_swap_benefici = [93904.0,93904.0,93904.0,93904.0,93904.0,93904.0,93904.0,93904.0,93904.0,93904.0]
treure_afegir_swap_benefici = [93904.0,93904.0,93904.0,93904.0,93904.0,93904.0,93904.0,93904.0,93904.0,93904.0]
moure_swap_benefici = [52136.0,52136.0,52136.0,52136.0,52136.0,52136.0,52136.0,52136.0,52136.0,52136.0]
treure_moure_swap_benefici = [52136.0,52136.0,52136.0,52136.0,52136.0,52136.0,52136.0,52136.0,52136.0,52136.0]
afegir_moure_swap_benefici = [93920.0,93920.0,93920.0,93920.0,93920.0,93920.0,93920.0,93920.0,93920.0,93920.0]
treure_afegir_moure_swap_benefici = [93920.0,93920.0,93920.0,93920.0,93920.0,93920.0,93920.0,93920.0,93920.0,93920.0]

# resumTemps:
temps = [9.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0,4.0]
treure_temps = [15.0,8.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0,5.0]
afegir_temps = [39.0,30.0,23.0,25.0,17.0,16.0,21.0,21.0,15.0,15.0]
treure_afegir_temps = [25.0,17.0,24.0,17.0,22.0,24.0,17.0,15.0,17.0,17.0]
moure_temps = [246.0,204.0,147.0,154.0,122.0,125.0,131.0,140.0,123.0,125.0]
treure_moure_temps = [134.0,135.0,135.0,138.0,134.0,144.0,132.0,134.0,139.0,138.0]
afegir_moure_temps = [46.0,43.0,42.0,43.0,46.0,44.0,53.0,43.0,44.0,46.0]
treure_afegir_moure_temps = [55.0,57.0,55.0,54.0,55.0,55.0,55.0,54.0,58.0,57.0]
swap_temps = [233.0,206.0,199.0,211.0,205.0,205.0,224.0,197.0,205.0,201.0]
treure_swap_temps = [208.0,201.0,226.0,249.0,235.0,263.0,351.0,323.0,321.0,304.0]
afegir_swap_temps = [1913.0,1989.0,1687.0,1533.0,1840.0,1631.0,1952.0,2330.0,1878.0,1612.0]
treure_afegir_swap_temps = [1493.0,1246.0,1178.0,1180.0,1502.0,1402.0,1834.0,1336.0,1430.0,1424.0]
moure_swap_temps = [365.0,331.0,352.0,355.0,344.0,335.0,328.0,342.0,336.0,344.0]
treure_moure_swap_temps = [348.0,356.0,347.0,349.0,344.0,349.0,345.0,347.0,343.0,362.0]
afegir_moure_swap_temps = [1346.0,1324.0,1344.0,1293.0,1272.0,1270.0,1277.0,1249.0,1278.0,1327.0]
treure_afegir_moure_swap_temps = [1412.0,1400.0,1367.0,1340.0,1303.0,1312.0,1331.0,1341.0,1337.0,1380.0]

'''segona vegada
# resumBenefici:
benefici = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
treure_benefici = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
afegir_benefici = [87180.0,87180.0,87180.0,87180.0,87180.0,87180.0,87180.0,87180.0,87180.0,87180.0]
treure_afegir_benefici = [87180.0,87180.0,87180.0,87180.0,87180.0,87180.0,87180.0,87180.0,87180.0,87180.0]
moure_benefici = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
treure_moure_benefici = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
afegir_moure_benefici = [91064.0,91064.0,91064.0,91064.0,91064.0,91064.0,91064.0,91064.0,91064.0,91064.0]
treure_afegir_moure_benefici = [91064.0,91064.0,91064.0,91064.0,91064.0,91064.0,91064.0,91064.0,91064.0,91064.0]
swap_benefici = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
treure_swap_benefici = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
afegir_swap_benefici = [94724.0,94724.0,94724.0,94724.0,94724.0,94724.0,94724.0,94724.0,94724.0,94724.0]
treure_afegir_swap_benefici = [94724.0,94724.0,94724.0,94724.0,94724.0,94724.0,94724.0,94724.0,94724.0,94724.0]
moure_swap_benefici = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
treure_moure_swap_benefici = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
afegir_moure_swap_benefici = [94788.0,94788.0,94788.0,94788.0,94788.0,94788.0,94788.0,94788.0,94788.0,94788.0]
treure_afegir_moure_swap_benefici = [94788.0,94788.0,94788.0,94788.0,94788.0,94788.0,94788.0,94788.0,94788.0,94788.0]

# resumTemps:
temps = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
treure_temps = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
afegir_temps = [280.0,208.0,197.0,199.0,194.0,201.0,194.0,217.0,210.0,199.0]
treure_afegir_temps = [228.0,222.0,218.0,228.0,224.0,227.0,217.0,229.0,226.0,220.0]
moure_temps = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
treure_moure_temps = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
afegir_moure_temps = [342.0,325.0,321.0,345.0,360.0,322.0,333.0,329.0,321.0,319.0]
treure_afegir_moure_temps = [335.0,346.0,352.0,334.0,351.0,362.0,349.0,362.0,350.0,336.0]
swap_temps = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
treure_swap_temps = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
afegir_swap_temps = [1416.0,1369.0,1374.0,1295.0,1370.0,1391.0,1301.0,1343.0,1351.0,1271.0]
treure_afegir_swap_temps = [1283.0,1299.0,1262.0,1277.0,1310.0,1307.0,1359.0,1377.0,1400.0,1346.0]
moure_swap_temps = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
treure_moure_swap_temps = [0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0]
afegir_moure_swap_temps = [1475.0,1486.0,1484.0,1469.0,1483.0,1508.0,1530.0,1408.0,1423.0,1403.0]
treure_afegir_moure_swap_temps = [1416.0,1494.0,1506.0,1427.0,1461.0,1434.0,1653.0,1569.0,1476.0,1740.0]
'''

'''tercera vegada

'''

beneficis = [benefici, treure_benefici, afegir_benefici, treure_afegir_benefici, moure_benefici,
             treure_moure_benefici, afegir_moure_benefici, treure_afegir_moure_benefici,
             swap_benefici, treure_swap_benefici, afegir_swap_benefici, treure_afegir_swap_benefici,
             moure_swap_benefici, treure_moure_swap_benefici, afegir_moure_swap_benefici,
             treure_afegir_moure_swap_benefici]

temps = [temps, treure_temps, afegir_temps, treure_afegir_temps, moure_temps,
             treure_moure_temps, afegir_moure_temps, treure_afegir_moure_temps,
             swap_temps, treure_swap_temps, afegir_swap_temps, treure_afegir_swap_temps,
             moure_swap_temps, treure_moure_swap_temps, afegir_moure_swap_temps,
             treure_afegir_moure_swap_temps]

beneficis_baixos = []
beneficis_mitjans = []
beneficis_alts = []

for i, b in enumerate(beneficis):
    mitjana = np.median(b)
    if mitjana < 60000: beneficis_baixos.append(b)
    elif mitjana < 80000: beneficis_mitjans.append(b)
    else: beneficis_alts.append(b)
    print(i, mitjana)

for i, t in enumerate(temps):
    mitjana = np.median(t)
    print(i, mitjana)


plt.boxplot(x=beneficis_baixos)
plt.show()
plt.boxplot(x=beneficis_mitjans)
plt.show()
plt.boxplot(x=beneficis_alts)
plt.show()

temps_baixos = []
temps_mitjans = []
temps_alts = []

for i, b in enumerate(temps):
    mitjana = np.median(b)
    if mitjana < 25: temps_baixos.append(b)
    elif mitjana < 400: temps_mitjans.append(b)
    else: temps_alts.append(b)
    print(i, mitjana)

for i, t in enumerate(temps):
    mitjana = np.median(t)
    print(i, mitjana)


plt.boxplot(x=temps_baixos)
plt.show()
plt.boxplot(x=temps_mitjans)
plt.show()
plt.boxplot(x=temps_alts)
plt.show()