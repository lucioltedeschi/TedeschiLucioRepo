from graphviz import Digraph


def pedir_datos_clases():
    clases = {}
    n = int(input("¿Cuántas clases querés crear? "))

    for _ in range(n):
        nombre = input("\nNombre de la clase: ")
        atributos = []
        metodos = []

        while True:
            attr = input("  Atributo (nombre:tipo) o enter para terminar: ")
            if not attr:
                break
            atributos.append(attr)

        while True:
            metodo = input("  Método (nombre()) o enter para terminar: ")
            if not metodo:
                break
            metodos.append(metodo)

        hereda = input("  ¿Hereda de alguna clase? (dejar vacío si no): ")

        clases[nombre] = {
            "atributos": atributos,
            "metodos": metodos
        }

        if hereda:
            clases[nombre]["hereda_de"] = hereda

    return clases


def generar_diagrama_clases(clases, filename="diagrama_clases"):
    dot = Digraph(comment='Diagrama de Clases', format='png')

    for clase, info in clases.items():
        label = f"<<B>{clase}</B><BR ALIGN='LEFT'/>"
        for attr in info.get("atributos", []):
            label += f"+ {attr}<BR ALIGN='LEFT'/>"
        for metodo in info.get("metodos", []):
            label += f"# {metodo}<BR ALIGN='LEFT'/>"
        label += ">"
        dot.node(clase, label=label, shape='record')

        if "hereda_de" in info:
            dot.edge(info["hereda_de"], clase, arrowhead="onormal")

    dot.render(filename, cleanup=True)
    print(f"\n✅ Diagrama de clases guardado como {filename}.png")


def pedir_datos_objetos(clases):
    objetos = {}
    n = int(input("\n¿Cuántos objetos querés crear? "))

    for _ in range(n):
        nombre_obj = input("\nNombre del objeto (ej: persona1): ")

        while True:
            clase = input("  Clase del objeto: ")
            if clase in clases:
                break
            print("  ❌ Esa clase no existe. Probá de nuevo.")

        atributos_clase = clases[clase]["atributos"]
        atributos_obj = {}

        for attr in atributos_clase:
            attr_nombre = attr.split(":")[0]
            valor = input(f"  Valor para '{attr_nombre}': ")
            atributos_obj[attr_nombre] = valor

        objetos[nombre_obj] = {
            "clase": clase,
            "atributos": atributos_obj
        }

    return objetos



def generar_diagrama_objetos(objetos, filename="diagrama_objetos"):
    dot = Digraph(comment='Diagrama de Objetos', format='png')

    for obj, info in objetos.items():
        clase = info["clase"]
        label = f"<<B>{obj} : {clase}</B><BR ALIGN='LEFT'/>"
        for k, v in info["atributos"].items():
            label += f"{k} = {v}<BR ALIGN='LEFT'/>"
        label += ">"
        dot.node(obj, label=label, shape='record')

    dot.render(filename, cleanup=True)
    print(f"\n✅ Diagrama de objetos guardado como {filename}.png")


# === PROGRAMA PRINCIPAL ===

if __name__ == "__main__":
    try:
        print("=== Generador de Diagramas UML ===\n")
        clases = pedir_datos_clases()
        generar_diagrama_clases(clases)

        opcion = input("\n¿Querés generar también un diagrama de objetos? (s/n): ").lower()
        if opcion == "s":
            objetos = pedir_datos_objetos(clases)
            generar_diagrama_objetos(objetos)
        else:
            print("\n🛑 Diagrama de objetos no generado.")
    except Exception as e:
        print(f"\n❌ Ocurrió un error: {e}")
    input("\nPresioná ENTER para cerrar...")
