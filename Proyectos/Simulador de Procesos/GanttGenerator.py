import matplotlib.pyplot as plt
from collections import deque
import copy

# Variable global para guardar procesos cargados
procesos_globales = []

def input_procesos(prioridades=False):
    procesos = []
    n = int(input("Cantidad de procesos: "))
    for _ in range(n):
        nombre = input("Nombre del proceso: ")
        llegada = int(input(f"Tiempo de llegada de {nombre}: "))
        rafaga = int(input(f"Ráfaga de CPU de {nombre}: "))
        prioridad = int(input(f"Prioridad de {nombre} (menor = más importante): ")) if prioridades else None
        procesos.append({'nombre': nombre, 'llegada': llegada, 'rafaga': rafaga, 'prioridad': prioridad})
    return procesos

def calcular_tiempos(resultados):
    tiempos = {}
    for p in resultados:
        nombre = p['nombre']
        if nombre not in tiempos:
            tiempos[nombre] = {'inicio': p['inicio'], 'fin': p['inicio'] + p['duracion']}
        else:
            tiempos[nombre]['fin'] = p['inicio'] + p['duracion']

    espera_total, retorno_total = 0, 0
    print("\nProceso\tEspera\tRetorno")
    for p in tiempos:
        llegada = next(proc['llegada'] for proc in procesos_globales if proc['nombre'] == p)
        rafaga = next(proc['rafaga'] for proc in procesos_globales if proc['nombre'] == p)
        espera = tiempos[p]['fin'] - llegada - rafaga
        retorno = tiempos[p]['fin'] - llegada
        espera_total += espera
        retorno_total += retorno
        print(f"{p}\t{espera}\t{retorno}")
    n = len(tiempos)
    print(f"\nPromedio espera: {espera_total / n:.2f}")
    print(f"Promedio retorno: {retorno_total / n:.2f}")

def dibujar_gantt(resultados, titulo):
    fig, ax = plt.subplots()
    ax.set_title(f"Diagrama de Gantt - {titulo}")
    ax.set_xlabel("Tiempo")

    procesos_nombres = sorted(list(set(p['nombre'] for p in resultados)))
    y_ticks = []
    y_labels = []

    for i, nombre in enumerate(procesos_nombres):
        y = i * 10
        y_ticks.append(y + 5)
        y_labels.append(nombre)
        bloques = [(p['inicio'], p['duracion']) for p in resultados if p['nombre'] == nombre]
        ax.broken_barh(bloques, (y, 9), facecolors='tab:blue')
        for b in bloques:
            ax.text(b[0] + b[1]/2 - 0.3, y + 4, nombre, color='white', ha='center', va='center', fontsize=8)

    ax.set_yticks(y_ticks)
    ax.set_yticklabels(y_labels)
    ax.set_ylim(0, len(procesos_nombres) * 10)
    ax.set_xlim(0, max(p['inicio'] + p['duracion'] for p in resultados) + 2)
    ax.set_xticks(range(0, int(ax.get_xlim()[1]) + 1, 2))
    ax.grid(True)
    plt.tight_layout()
    plt.show()

def fcfs(procesos):
    p_sorted = sorted(procesos, key=lambda x: x['llegada'])
    tiempo = 0
    resultado = []
    for p in p_sorted:
        if tiempo < p['llegada']:
            tiempo = p['llegada']
        resultado.append({'nombre': p['nombre'], 'inicio': tiempo, 'duracion': p['rafaga']})
        tiempo += p['rafaga']
    return resultado

def sjf(procesos):
    tiempo = 0
    lista = copy.deepcopy(procesos)
    resultado = []
    completados = []

    while len(completados) < len(procesos):
        disponibles = [p for p in lista if p['llegada'] <= tiempo and p['nombre'] not in completados]
        if not disponibles:
            tiempo += 1
            continue
        elegido = min(disponibles, key=lambda x: x['rafaga'])
        resultado.append({'nombre': elegido['nombre'], 'inicio': tiempo, 'duracion': elegido['rafaga']})
        tiempo += elegido['rafaga']
        completados.append(elegido['nombre'])
    return resultado

def srtf(procesos):
    lista = copy.deepcopy(procesos)
    tiempo = 0
    en_cola = []
    resultado = []
    ejecutando = None
    rafagas_restantes = {p['nombre']: p['rafaga'] for p in lista}
    proceso_actual = None
    start_time = {}

    while True:
        for p in lista:
            if p['llegada'] == tiempo:
                en_cola.append(p)

        if en_cola:
            ejecutando = min(en_cola, key=lambda x: rafagas_restantes[x['nombre']])
            if proceso_actual != ejecutando['nombre']:
                proceso_actual = ejecutando['nombre']
                start_time[proceso_actual] = tiempo
            resultado.append({'nombre': proceso_actual, 'inicio': tiempo, 'duracion': 1})
            rafagas_restantes[proceso_actual] -= 1
            if rafagas_restantes[proceso_actual] == 0:
                en_cola = [p for p in en_cola if p['nombre'] != proceso_actual]
                proceso_actual = None
        else:
            if all(rafagas_restantes[p['nombre']] == 0 for p in lista):
                break
        tiempo += 1

        if all(rafagas_restantes[p['nombre']] == 0 for p in lista):
            break

    fusionado = []
    for bloque in resultado:
        if fusionado and fusionado[-1]['nombre'] == bloque['nombre'] and fusionado[-1]['inicio'] + fusionado[-1]['duracion'] == bloque['inicio']:
            fusionado[-1]['duracion'] += 1
        else:
            fusionado.append(bloque)
    return fusionado

def round_robin(procesos, quantum=2):
    lista = copy.deepcopy(procesos)
    tiempo = 0
    cola = deque()
    resultado = []
    rafaga_restante = {p['nombre']: p['rafaga'] for p in lista}
    llegados = set()
    completados = set()

    while lista or cola:
        for p in lista:
            if p['llegada'] <= tiempo and p['nombre'] not in llegados:
                cola.append(p['nombre'])
                llegados.add(p['nombre'])

        if cola:
            actual = cola.popleft()
            duracion = min(quantum, rafaga_restante[actual])
            resultado.append({'nombre': actual, 'inicio': tiempo, 'duracion': duracion})
            tiempo += duracion
            rafaga_restante[actual] -= duracion
            for p in lista:
                if tiempo - duracion < p['llegada'] <= tiempo and p['nombre'] not in llegados:
                    cola.append(p['nombre'])
                    llegados.add(p['nombre'])
            if rafaga_restante[actual] > 0:
                cola.append(actual)
            else:
                completados.add(actual)
        else:
            tiempo += 1
        lista = [p for p in lista if p['nombre'] not in completados]
    return resultado

def por_prioridades(procesos):
    tiempo = 0
    lista = copy.deepcopy(procesos)
    resultado = []
    completados = []

    while len(completados) < len(procesos):
        disponibles = [p for p in lista if p['llegada'] <= tiempo and p['nombre'] not in completados]
        disponibles = [p for p in disponibles if p['prioridad'] is not None]
        if not disponibles:
            tiempo += 1
            continue
        elegido = min(disponibles, key=lambda x: x['prioridad'])
        resultado.append({'nombre': elegido['nombre'], 'inicio': tiempo, 'duracion': elegido['rafaga']})
        tiempo += elegido['rafaga']
        completados.append(elegido['nombre'])
    return resultado

def comparar_todos(procesos, quantum):
    resumen = []
    for nombre, algoritmo in [
        ("FCFS", lambda p: fcfs(p)),
        ("SJF", lambda p: sjf(p)),
        ("SRTF", lambda p: srtf(p)),
        (f"Round Robin (Q={quantum})", lambda p: round_robin(p, quantum))
    ]:
        print(f"\n======= {nombre} =======")
        resultado = algoritmo(copy.deepcopy(procesos))
        calcular_tiempos(resultado)
        dibujar_gantt(resultado, nombre)

        tiempos = {}
        for p in resultado:
            if p['nombre'] not in tiempos:
                tiempos[p['nombre']] = {'inicio': p['inicio'], 'fin': p['inicio'] + p['duracion']}
            else:
                tiempos[p['nombre']]['fin'] = p['inicio'] + p['duracion']

        espera_total, retorno_total = 0, 0
        for proc in tiempos:
            llegada = next(pr['llegada'] for pr in procesos if pr['nombre'] == proc)
            rafaga = next(pr['rafaga'] for pr in procesos if pr['nombre'] == proc)
            espera = tiempos[proc]['fin'] - llegada - rafaga
            retorno = tiempos[proc]['fin'] - llegada
            espera_total += espera
            retorno_total += retorno

        n = len(tiempos)
        resumen.append({
            'Algoritmo': nombre,
            'Espera promedio': espera_total / n,
            'Retorno promedio': retorno_total / n
        })

    print("\n===== COMPARACIÓN GENERAL =====")
    for r in resumen:
        print(f"{r['Algoritmo']:<25} | Espera: {r['Espera promedio']:.2f} | Retorno: {r['Retorno promedio']:.2f}")

# Menú de selección
print("Simuladores disponibles:")
print("1. FCFS")
print("2. SJF")
print("3. SRTF")
print("4. Round Robin")
print("5. Por Prioridades")
print("6. Comparar todos los algoritmos (sin prioridades)")
print("7. Generar todos los diagramas individualmente (sin prioridades)")

opcion = int(input("Elegí una opción (1-7): "))

procesos = input_procesos()

while opcion != 0:

    if opcion == 6:
        procesos_globales = copy.deepcopy(procesos)
        quantum = int(input("Quantum para Round Robin: "))
        comparar_todos(procesos, quantum)

    elif opcion == 7:

        procesos_globales = copy.deepcopy(procesos)

        print("\nGenerando todos los diagramas individuales...")
        for nombre, func in [("FCFS", fcfs), ("SJF", sjf), ("SRTF", srtf)]:
            resultado = func(copy.deepcopy(procesos))
            print(f"\n=== {nombre} ===")
            calcular_tiempos(resultado)
            dibujar_gantt(resultado, nombre)
        flag = True
        while flag == True:
            if flag == True:
                quantum = int(input("Quantum para Round Robin (0 para salir): "))
            elif quantum == 0:
                flag = False
                
                
            while quantum != 0:
                resultado_rr = round_robin(copy.deepcopy(procesos), quantum)
                print(f"\n=== Round Robin (Q={quantum}) ===")
                calcular_tiempos(resultado_rr)
                dibujar_gantt(resultado_rr, f"Round Robin (Q={quantum})")
                quantum = int(input("Quantum para Round Robin (0 para salir): "))

            

    elif opcion == 5:
        procesos = input_procesos(prioridades=True)
        procesos_globales = copy.deepcopy(procesos)
        resultado = por_prioridades(procesos)
        print("\n================= Por Prioridades =================")
        calcular_tiempos(resultado)
        dibujar_gantt(resultado, "Por Prioridades")

    else:
        prioridades = False
        procesos_globales = copy.deepcopy(procesos)

        if opcion == 1:
            resultado = fcfs(procesos)
            nombre = "FCFS"
        elif opcion == 2:
            resultado = sjf(procesos)
            nombre = "SJF"
        elif opcion == 3:
            resultado = srtf(procesos)
            nombre = "SRTF"
        elif opcion == 4:
            while True:
                quantum = int(input("Quantum para Round Robin (0 para salir): "))
                if quantum == 0:
                    break
                resultado = round_robin(procesos, quantum)
                print(f"\n================= Round Robin (Q={quantum}) =================")
                calcular_tiempos(resultado)
                dibujar_gantt(resultado, f"Round Robin (Q={quantum})")
            exit()
        else:
            print("Opcion no valida")
            exit()

        print(f"\n================= {nombre} =================")
        calcular_tiempos(resultado)
        dibujar_gantt(resultado, nombre)
        
    print("Simuladores disponibles:")
    print("1. FCFS")
    print("2. SJF")
    print("3. SRTF")
    print("4. Round Robin")
    print("5. Por Prioridades")
    print("6. Comparar todos los algoritmos (sin prioridades)")
    print("7. Generar todos los diagramas individualmente (sin prioridades)")

    opcion = int(input("Elegí una opción (1-7): "))