import { ChangeDetectionStrategy, Component, OnInit, ViewChild } from '@angular/core';
import { BehaviorSubject, combineLatest, Observable, switchMap } from 'rxjs';
import { Producto } from '../../../../core/interfaces/interfaces';
import { KpiData, DashboardService } from '../../services/dashboard.service';
import { CommonModule, DecimalPipe } from '@angular/common';
import { ApexGrid, ChartComponent, NgApexchartsModule } from 'ng-apexcharts';
import { ApiService } from '../../../../core/services/api.service';

function toISODateTime(date: Date, endOfDay = false): string {
  if (endOfDay) {
    date.setHours(23, 59, 59, 999);
  } else {
    date.setHours(0, 0, 0, 0);
  }
  return date.toISOString().split('.')[0];
}

export type ChartOptions = {
  series: ApexAxisChartSeries;
  chart: ApexChart & { type: 'area' };
  xaxis: ApexXAxis;
  stroke: ApexStroke;
  fill: ApexFill;
  grid: ApexGrid;
};

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, DecimalPipe, NgApexchartsModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss',
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class Dashboard implements OnInit {
  private fechaInicio$ = new BehaviorSubject<string>(
    toISODateTime(new Date(Date.now() - 29 * 86400000))
  );
  private fechaFin$ = new BehaviorSubject<string>(
    toISODateTime(new Date(), true)
  );

  kpis$!: Observable<KpiData>;
  sinStock$!: Observable<Producto[]>;
  stockBajo$!: Observable<Producto[]>;

  @ViewChild('chart') chart!: ChartComponent;

  public chartOptions: ChartOptions = {
    series: [
      {
        name: 'Ventas',
        data: []
      }
    ],
    chart: {
      type: 'area',
      height: 280,
      toolbar: { show: false },
      animations: { enabled: true },
      zoom: { enabled: false }
    },
    stroke: {
      curve: 'smooth',
      width: 3
    },
    fill: {
      type: 'gradient',
      gradient: {
        shadeIntensity: 0.5,
        opacityFrom: 0.45,
        opacityTo: 0.05,
        stops: [0, 90, 100]
      }
    },
    xaxis: {
      categories: [],
      labels: { style: { colors: '#b9d0dc' } }
    },
    grid: {
      show: false
    }
  };

  constructor(private dashboard: DashboardService, public api: ApiService) { }

  ngOnInit(): void {
    this.kpis$ = combineLatest([this.fechaInicio$, this.fechaFin$]).pipe(
      switchMap(([ini, fin]) => this.dashboard.loadKpis(ini, fin))
    );

    this.sinStock$ = this.dashboard.getProductosSinStock();
    this.stockBajo$ = this.dashboard.getProductosStockBajo();

    combineLatest([this.fechaInicio$, this.fechaFin$])
      .pipe(
        switchMap(([ini, fin]) =>
          this.dashboard.getVentasHistoricas(ini, fin)
        )
      )
      .subscribe((resp) => {

        this.chartOptions = {
          ...this.chartOptions,
          series: [
            {
              name: 'Ventas',
              data: resp.valores
            }
          ],
          xaxis: {
            categories: resp.fechas,
            labels: { style: { colors: '#b9d0dc' } }
          }
        };

      });
      this.setRango(7);
  }

  setRango(dias: number): void {
    const fin = new Date();
    const ini = new Date(Date.now() - (dias - 1) * 86400000);

    this.fechaInicio$.next(toISODateTime(ini));
    this.fechaFin$.next(toISODateTime(fin, true));
  }
}
